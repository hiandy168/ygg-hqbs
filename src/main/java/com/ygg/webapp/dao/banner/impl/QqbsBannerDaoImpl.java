
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.webapp.dao.banner.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.webapp.dao.banner.QqbsBannerDao;
import com.ygg.webapp.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.webapp.entity.QqbsBannerWindowEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsBannerDaoImpl.java 12586 2016-05-23 07:16:23Z qiuyibo $   
  * @since 2.0
  */
@Repository("qqbsBannerDao")
public class QqbsBannerDaoImpl extends BaseDaoImpl implements QqbsBannerDao {
	
	
	 public List<QqbsBannerWindowEntity> findAllBrandByPara(Map<String, Object> para)
		        throws Exception{
        List<QqbsBannerWindowEntity> resultList = getSqlSession().selectList("QqbsBannerMapper.findAllBrandByPara", para);
        if (resultList == null)
        {
            return new ArrayList<QqbsBannerWindowEntity>();
        }
        return resultList;
    }
}
