package com.example.demo.news;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class NewsForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private int id;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String content;

	@Getter
	@Setter
	private String kokaistartdate;

	@Getter
	@Setter
	private String kokaienddate;

	@Getter
	@Setter
	private int shitenid;

	@Getter
	@Setter
	private int kokaiflg;

	@Getter
	@Setter
	private String shitenname;
}
