package com.fisincorporated.utility;


public class ThreadDemoObject {
  
		private int timePeriod;
		private String id;
		
		private ThreadDemoObject(){
			;
		}
		private ThreadDemoObject( String id, int timePeriod){
			this.timePeriod = timePeriod;
			this.id = id;
		}
		public static ThreadDemoObject newInstance( String id, int timePeriod){
			return new  ThreadDemoObject(id , timePeriod);
		}
		public int getTimePeriod(){
			return timePeriod;
		}
		public String getId(){
			return id;
		}
	}

 