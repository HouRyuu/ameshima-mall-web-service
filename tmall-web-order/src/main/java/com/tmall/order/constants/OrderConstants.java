package com.tmall.order.constants;

public class OrderConstants {

    public enum OrderMqState {
        ERROR(-1), DEFAULT(0), OK(1);

        private int state;

        OrderMqState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }


}
