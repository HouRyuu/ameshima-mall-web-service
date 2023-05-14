package com.ameshima.order.constants;

public class OrderConstants {

    public enum OrderMqState {
        ERROR(-1), DEFAULT(0), OK(1);

        private final int state;

        OrderMqState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

}
