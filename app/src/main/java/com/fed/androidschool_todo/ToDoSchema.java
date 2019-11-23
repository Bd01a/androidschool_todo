package com.fed.androidschool_todo;

class ToDoSchema {
    static final String NAME = "ToDo";

    static final class Cols {
        static final String ACTIVITY = "activity";
        static final String FLAG = "flag";
    }

    public static class ToDoElement{
        String mActivity;
        boolean mFlag;

        ToDoElement(String name, int flag) {
            mActivity = name;
            mFlag = flag != 0;
        }

        String getActivity() {
            return mActivity;
        }

        public void setActivity(String name) {
            mActivity = name;
        }

        boolean isFlag() {
            return mFlag;
        }

        void setFlag(boolean flag) {
            mFlag = flag;
        }
    }
}
