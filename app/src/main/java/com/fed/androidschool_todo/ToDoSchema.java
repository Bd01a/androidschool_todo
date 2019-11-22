package com.fed.androidschool_todo;

public class ToDoSchema {
    public static final String NAME = "ToDo";
    public static final class Cols{
        public static final String ACTIVITY = "activity";
        public static final String FLAG = "flag";
    }

    public static class ToDoElement{
        String mActivity;
        boolean mFlag;

        public ToDoElement(String name, int flag) {
            mActivity = name;
            mFlag = flag != 0;
        }

        public String getActivity() {
            return mActivity;
        }

        public void setActivity(String name) {
            mActivity = name;
        }

        public boolean isFlag() {
            return mFlag;
        }

        public void setFlag(boolean flag) {
            mFlag = flag;
        }
    }
}
