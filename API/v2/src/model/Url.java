package model;

public class Url {
	private String url;
	private int UrlId = -1;
	private int method = -1;
	private int extension = -1;

	public int getUrlId() {
		return UrlId;
	}

	public void setUrlId(int urlId) {
		UrlId = urlId;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getExtension() {
		return extension;
	}

	public void setExtension(int extension) {
		this.extension = extension;
	}
}
