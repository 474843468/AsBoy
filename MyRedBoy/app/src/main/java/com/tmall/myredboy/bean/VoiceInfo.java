package com.tmall.myredboy.bean;

import java.util.ArrayList;

public class VoiceInfo {

	public int bg;
	public int ed;
	public boolean ls;
	public int sn;

	public ArrayList<WSInfo> ws;

	public class WSInfo {
		public int bg;
		public ArrayList<CWInfo> cw;

	}

	public class CWInfo {

		public float sc;
		public String w;

	}
}
