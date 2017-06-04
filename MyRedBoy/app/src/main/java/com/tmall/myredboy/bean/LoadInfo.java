package com.tmall.myredboy.bean;

import java.io.Serializable;

/**
     * Created by Administrator on 2016/11/16 0016.
     */

    public class LoadInfo implements Serializable{

        /**
         * message : 登陆成功！
         * status : 200
         * user : {"collectionCount":0,"email":"zs","id":1395,"loginTime":1479271558090,"memberLevel":0,"orderCount":0,"password":"","userScore":0,"username":"zs"}
         */

        public String message;       //登录状态
        public int      status;    //状态码
        public UserBean user;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements  Serializable{
            /**
             * collectionCount : 0
             * email : zs
             * id : 1395
             * loginTime : 1479271558090
             * memberLevel : 0
             * orderCount : 0
             * password : 
             * userScore : 0
             * username : zs
             */

            public int collectionCount;
            public String email;
            public int    id;
            public long   loginTime;
            public int    memberLevel;
            public int    orderCount;
            public String password;
            public int    userScore;
            public String username;

            public int getCollectionCount() {
                return collectionCount;
            }

            public void setCollectionCount(int collectionCount) {
                this.collectionCount = collectionCount;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getLoginTime() {
                return loginTime;
            }

            public void setLoginTime(long loginTime) {
                this.loginTime = loginTime;
            }

            public int getMemberLevel() {
                return memberLevel;
            }

            public void setMemberLevel(int memberLevel) {
                this.memberLevel = memberLevel;
            }

            public int getOrderCount() {
                return orderCount;
            }

            public void setOrderCount(int orderCount) {
                this.orderCount = orderCount;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getUserScore() {
                return userScore;
            }

            public void setUserScore(int userScore) {
                this.userScore = userScore;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
