package com.hussain.smalltalk;

/**
 * Created by Hussain on 31-Jul-17.
 */

public class ConversationHelper {
        private String message;
        private String sender;

        public ConversationHelper()
        {
        }

        public ConversationHelper(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }


}
