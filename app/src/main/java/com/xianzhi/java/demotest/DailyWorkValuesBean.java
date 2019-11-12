package com.xianzhi.java.demotest;

import java.io.Serializable;
import java.util.List;

/**
 * @author LiMing
 * @Demo class DailyWorkValuesBean
 * @Description TODO
 * @date 2019/9/12 上午10:27
 */

public class DailyWorkValuesBean implements Serializable {

    private String info;
    private List<QuestionListBean> questionList;
    private List<LeaderListBean> leaderList;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<QuestionListBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListBean> questionList) {
        this.questionList = questionList;
    }

    public List<LeaderListBean> getLeaderList() {
        return leaderList;
    }

    public void setLeaderList(List<LeaderListBean> leaderList) {
        this.leaderList = leaderList;
    }

    public static class QuestionListBean {
        /**
         * id : 468
         * desc : 你今天是否在用“诚信问题零容忍”来要求自己？
         * key : 1
         * val : null
         * pri : 0
         * start_date : 1567267200000
         * end_date : 4133865600000
         * type : 1
         * is_share : 0
         * deleted : 0
         * answerList : [{"id":471,"desc":"A.完全达到。","key":1,"val":1,"pri":468,"start_date":1567267200000,"end_date":4133865600000,"type":0,"is_share":0,"deleted":0},{"id":472,"desc":"B.部分达到。","key":2,"val":0,"pri":468,"start_date":1567267200000,"end_date":4133865600000,"type":0,"is_share":0,"deleted":0},{"id":473,"desc":"C.没有达到。","key":3,"val":-1,"pri":468,"start_date":1567267200000,"end_date":4133865600000,"type":0,"is_share":0,"deleted":0}]
         */

        private int id;
        private String desc;
        private int key;
        private Object val;
        private int pri;
        private long start_date;
        private long end_date;
        private int type;
        private int is_share;
        private int deleted;
        private List<AnswerListBean> answerList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

        public int getPri() {
            return pri;
        }

        public void setPri(int pri) {
            this.pri = pri;
        }

        public long getStart_date() {
            return start_date;
        }

        public void setStart_date(long start_date) {
            this.start_date = start_date;
        }

        public long getEnd_date() {
            return end_date;
        }

        public void setEnd_date(long end_date) {
            this.end_date = end_date;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIs_share() {
            return is_share;
        }

        public void setIs_share(int is_share) {
            this.is_share = is_share;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public List<AnswerListBean> getAnswerList() {
            return answerList;
        }

        public void setAnswerList(List<AnswerListBean> answerList) {
            this.answerList = answerList;
        }

        public static class AnswerListBean {
            /**
             * id : 471
             * desc : A.完全达到。
             * key : 1
             * val : 1
             * pri : 468
             * start_date : 1567267200000
             * end_date : 4133865600000
             * type : 0
             * is_share : 0
             * deleted : 0
             */

            private int id;
            private String desc;
            private int key;
            private int val;
            private int pri;
            private long start_date;
            private long end_date;
            private int type;
            private int is_share;
            private boolean isSelect;
            private int deleted;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getKey() {
                return key;
            }

            public void setKey(int key) {
                this.key = key;
            }

            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }

            public int getPri() {
                return pri;
            }

            public void setPri(int pri) {
                this.pri = pri;
            }

            public long getStart_date() {
                return start_date;
            }

            public void setStart_date(long start_date) {
                this.start_date = start_date;
            }

            public long getEnd_date() {
                return end_date;
            }

            public void setEnd_date(long end_date) {
                this.end_date = end_date;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getIs_share() {
                return is_share;
            }

            public void setIs_share(int is_share) {
                this.is_share = is_share;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }
        }
    }

    public static class LeaderListBean {
        /**
         * leaderDepName : 总裁办
         * leaderDid : 1
         * leader : 1
         * leaderName : 王谦
         */

        private String leaderDepName;
        private int leaderDid;
        private String leader;
        private String leaderName;

        public String getLeaderDepName() {
            return leaderDepName;
        }

        public void setLeaderDepName(String leaderDepName) {
            this.leaderDepName = leaderDepName;
        }

        public int getLeaderDid() {
            return leaderDid;
        }

        public void setLeaderDid(int leaderDid) {
            this.leaderDid = leaderDid;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getLeaderName() {
            return leaderName;
        }

        public void setLeaderName(String leaderName) {
            this.leaderName = leaderName;
        }
    }
}
