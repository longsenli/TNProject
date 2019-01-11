package com.tnpy.mes.model.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbMenu {
//    private Integer menuId;
//
//    private String menuName;
//
//    private Integer parentId;
//
//    private Integer orderNum;
//
//    private String url;
//
//    private String menuType;
//
//    private String visible;
//
//    private String perms;
//
//    private String icon;
//
//    private String createBy;
//
//    private Date createTime;
//
//    private String updateBy;
//
//    private Date updateTime;
//
//    private String remark;
//
//    public Integer getMenuId() {
//        return menuId;
//    }
//
//    public void setMenuId(Integer menuId) {
//        this.menuId = menuId;
//    }
//
//    public String getMenuName() {
//        return menuName;
//    }
//
//    public void setMenuName(String menuName) {
//        this.menuName = menuName == null ? null : menuName.trim();
//    }
//
//    public Integer getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(Integer parentId) {
//        this.parentId = parentId;
//    }
//
//    public Integer getOrderNum() {
//        return orderNum;
//    }
//
//    public void setOrderNum(Integer orderNum) {
//        this.orderNum = orderNum;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url == null ? null : url.trim();
//    }
//
//    public String getMenuType() {
//        return menuType;
//    }
//
//    public void setMenuType(String menuType) {
//        this.menuType = menuType == null ? null : menuType.trim();
//    }
//
//    public String getVisible() {
//        return visible;
//    }
//
//    public void setVisible(String visible) {
//        this.visible = visible == null ? null : visible.trim();
//    }
//
//    public String getPerms() {
//        return perms;
//    }
//
//    public void setPerms(String perms) {
//        this.perms = perms == null ? null : perms.trim();
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon == null ? null : icon.trim();
//    }
//
//    public String getCreateBy() {
//        return createBy;
//    }
//
//    public void setCreateBy(String createBy) {
//        this.createBy = createBy == null ? null : createBy.trim();
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getUpdateBy() {
//        return updateBy;
//    }
//
//    public void setUpdateBy(String updateBy) {
//        this.updateBy = updateBy == null ? null : updateBy.trim();
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark == null ? null : remark.trim();
//    }
    private static final long serialVersionUID = 1L;
    /** 菜单ID */
    private Long menuId;
    /** 菜单名称 */
    private String menuName;
    /** 父菜单名称 */
    private String parentName;
    /** 父菜单ID */
    private Long parentId;
    /** 显示顺序 */
    private String orderNum;
    /** 菜单URL */
    private String url;
    /** 类型:0目录,1菜单,2按钮 */
    private String menuType;
    /** 菜单状态:0显示,1隐藏 */
    private int visible;
    /** 权限字符串 */
    private String perms;
    /** 菜单图标 */
    private String icon;
    
  private String createBy;

  private Date createTime;

  private String updateBy;

  private Date updateTime;

  private String remark;
    
    public String getCreateBy() {
	return createBy;
}

public void setCreateBy(String createBy) {
	this.createBy = createBy;
}

public Date getCreateTime() {
	return createTime;
}

public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

public String getUpdateBy() {
	return updateBy;
}

public void setUpdateBy(String updateBy) {
	this.updateBy = updateBy;
}

public Date getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public static long getSerialversionuid() {
	return serialVersionUID;
}

	/** 子菜单 */
    private List<TbMenu> children = new ArrayList<TbMenu>();

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public int getVisible()
    {
        return visible;
    }

    public void setVisible(int visible)
    {
        this.visible = visible;
    }

    public String getPerms()
    {
        return perms;
    }

    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public List<TbMenu> getChildren()
    {
        return children;
    }

    public void setChildren(List<TbMenu> children)
    {
        this.children = children;
    }

    @Override
    public String toString()
    {
        return "Menu [menuId=" + menuId + ", menuName=" + menuName + ", parentName=" + parentName + ", parentId="
                + parentId + ", orderNum=" + orderNum + ", url=" + url + ", menuType=" + menuType + ", visible="
                + visible + ", perms=" + perms + ", icon=" + icon + ", children=" + children + "]";
    }
}