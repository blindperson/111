package cn.wizzer.app.web.commons.ext.wx;

import cn.wizzer.app.wx.modules.models.Wx_tpl_id;
import cn.wizzer.app.wx.modules.models.Wx_tpl_log;
import cn.wizzer.app.wx.modules.models.Wx_user;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxTplIdService;
import cn.wizzer.app.wx.modules.services.WxTplLogService;
import cn.wizzer.app.wx.modules.services.WxUserService;
import org.nutz.boot.starter.literpc.annotation.RpcInject;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.weixin.bean.WxTemplateData;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import java.util.Map;

/**
 * Created by wizzer on 2016/8/6.
 */
@IocBean
public class TplService {
    @RpcInject
    private WxTplIdService wxTplIdService;
    @RpcInject
    private WxUserService wxUserService;
    @RpcInject
    private WxTplLogService wxTplLogService;
    @RpcInject
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    /**
     * 通过模板编号发送模板消息，并记录到日志表
     *
     * @param wxid
     * @param openid
     * @param tplId
     * @param url
     * @param data
     * @return
     */
    public String send(String wxid, String openid, String tplId, String url, Map<String, WxTemplateData> data) {
        WxApi2 wxApi2 = wxService.getWxApi2(wxid);
        Wx_tpl_id tpl = wxTplIdService.fetch(tplId);
        if (tpl != null) {
            WxResp wxResp = wxApi2.template_send(openid, tpl.getTemplate_id(), url, data);
            Wx_user user = wxUserService.fetch(Cnd.where("openid", "=", openid));
            Wx_tpl_log l = new Wx_tpl_log();
            l.setWxid(wxid);
            l.setOpenid(openid);
            l.setNickname("");
            if (user != null) {
                l.setNickname(user.getNickname());
            }
            l.setContent(Json.toJson(data));
            if (wxResp.errcode() == 0) {
                l.setStatus(1);//发送成功
            } else l.setStatus(2);//发送失败
            Wx_tpl_log rl = wxTplLogService.insert(l);
            return rl == null ? null : rl.getId();
        }
        return null;
    }
}
