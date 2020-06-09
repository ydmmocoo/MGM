package com.library.repository.models;

import com.library.repository.db.model.ListBeanConverter;
import com.library.repository.db.model.MomentsInfoBeanConverter;
import com.library.repository.db.model.PraiseListConverter;
import com.library.repository.db.model.ReplyListBeanConverter;
import com.library.repository.db.model.ShareInfoBeanConverter;
import com.library.repository.db.model.StringConverter;
import com.library.repository.db.model.UserInfoBeanConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class MomentsListBean {

    @Convert(converter = MomentsInfoBeanConverter.class, columnType = String.class)
    private MomentsInfoBean momentsInfo;
    @Convert(converter = UserInfoBeanConverter.class, columnType = String.class)
    private UserInfoBean userInfo;
    @Convert(converter = ReplyListBeanConverter.class, columnType = String.class)
    private ReplyListBean replyList;
    @Property
    @Convert(converter = PraiseListConverter.class, columnType = String.class)
    private List<PraiseListBean> praiseList;
    @Convert(converter = StringConverter.class, columnType = String.class)
    private List<String> typeList;


    public MomentsInfoBean getMomentsInfo() {
        return momentsInfo;
    }

    public void setMomentsInfo(MomentsInfoBean momentsInfo) {
        this.momentsInfo = momentsInfo;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public ReplyListBean getReplyList() {
        return replyList;
    }

    public void setReplyList(ReplyListBean replyList) {
        this.replyList = replyList;
    }

    public List<PraiseListBean> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<PraiseListBean> praiseList) {
        this.praiseList = praiseList;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }


    public static class MomentsInfoBean {

        private String mId;
        private String content;
        private String createTime;
        private String address;
        private String distance;
        private String distanceUnit;
        @Convert(converter = StringConverter.class, columnType = String.class)
        private List<String> thumUrls;//视频或者封面地址缩略图地址
        @Convert(converter = StringConverter.class, columnType = String.class)
        private List<String> urls;
        @Convert(converter = ShareInfoBeanConverter.class, columnType = String.class)
        private ShareInfoBean shareInfo;

        public List<String> getThumUrls() {
            return thumUrls;
        }

        public void setThumUrls(List<String> thumUrls) {
            this.thumUrls = thumUrls;
        }

        public ShareInfoBean getShareInfo() {
            return shareInfo;
        }

        public void setShareInfo(ShareInfoBean shareInfo) {
            this.shareInfo = shareInfo;
        }

        public String getMId() {
            return mId;
        }

        public void setMId(String mId) {
            this.mId = mId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDistanceUnit() {
            return distanceUnit;
        }

        public void setDistanceUnit(String distanceUnit) {
            this.distanceUnit = distanceUnit;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public static class ShareInfoBean {

            private String shareType;//分享分类，1：黄页，2：新闻，3：同城
            private String shareId;//记录id
            private String title;//标题
            private String desc;//内容
            private String img;//封面
            private String typeName;//同城类型(当分享类型为同城时候此参数才不为空)
            private String typeId;//同城类型id(当分享类型为同城时候此参数才不为空)

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getTypeId() {
                return typeId;
            }

            public void setTypeId(String typeId) {
                this.typeId = typeId;
            }

            public String getShareType() {
                return shareType;
            }

            public void setShareType(String shareType) {
                this.shareType = shareType;
            }

            public String getShareId() {
                return shareId;
            }

            public void setShareId(String shareId) {
                this.shareId = shareId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }

    public static class UserInfoBean {

        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private String sex;
        @Convert(converter = StringConverter.class, columnType = String.class)
        private List<String> tags;

        public String getUserIdfer() {
            return userIdfer;
        }

        public void setUserIdfer(String userIdfer) {
            this.userIdfer = userIdfer;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getUserAvatar() {
            try {
                return URLDecoder.decode(userAvatar, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    public static class ReplyListBean {

        private boolean hasNext;
        @Convert(converter = ListBeanConverter.class, columnType = String.class)
        private List<ListBean> list;

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
    }

    public static class ListBean {
        private String replyId;
        private String content;
        private String createTime;
        private String LikeNum;
        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private String toUserNick;
        private String toReplyCont;
        private String toUserIdfer;
        private int flag;
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToUserIdfer() {
            return toUserIdfer;
        }

        public void setToUserIdfer(String toUserIdfer) {
            this.toUserIdfer = toUserIdfer;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLikeNum() {
            return LikeNum;
        }

        public void setLikeNum(String likeNum) {
            LikeNum = likeNum;
        }

        public String getUserIdfer() {
            return userIdfer;
        }

        public void setUserIdfer(String userIdfer) {
            this.userIdfer = userIdfer;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getToUserNick() {
            return toUserNick;
        }

        public void setToUserNick(String toUserNick) {
            this.toUserNick = toUserNick;
        }

        public String getToReplyCont() {
            return toReplyCont;
        }

        public void setToReplyCont(String toReplyCont) {
            this.toReplyCont = toReplyCont;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }

    public static class PraiseListBean {

        private String pId;
        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private Boolean last = false;

        public Boolean getLast() {
            return last;
        }

        public void setLast(Boolean last) {
            this.last = last;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
        }

        public String getUserIdfer() {
            return userIdfer;
        }

        public void setUserIdfer(String userIdfer) {
            this.userIdfer = userIdfer;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }
    }


}
