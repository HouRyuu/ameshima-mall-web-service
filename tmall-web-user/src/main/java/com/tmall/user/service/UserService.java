package com.tmall.user.service;

import com.tmall.common.constants.IErrResult;
import com.tmall.common.dto.AjaxResult;
import com.tmall.user.entity.dto.RegisterDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface UserService {

    AjaxResult register(RegisterDTO registerInfo);

}
