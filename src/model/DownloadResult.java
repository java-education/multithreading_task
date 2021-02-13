package model;

public class DownloadResult {
	public final int id;
	private final int data;

	public DownloadResult(int id, int data) {
		this.id = id;
		this.data = data;
	}

	public boolean check(int i) {
		return i == data;
	}
}
