package cn.wizzer.app.sys.modules.services;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.framework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/22.
 */
@RpcService
public interface SysUnitService extends BaseService<Sys_unit> {
    /**
     * 保存单位
     * @param unit
     * @param pid
     */
    void save(Sys_unit unit, String pid);

    /**
     * 级联删除单位及单位下用户
     * @param unit
     */
    void deleteAndChild(Sys_unit unit);
}
