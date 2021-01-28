package br.inpe.cap.asniffer.output.json.d3hierarchy.systemview;

import com.google.gson.annotations.SerializedName;

public class AnnotationReportSystemView {
	
	@SerializedName(value = "name")
	private String schema;
	@SerializedName(value = "size")
	private int qtd;

	
	public AnnotationReportSystemView(String schema, int qtd) {
		this.schema = schema;
		this.qtd = qtd;
	}


	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public int getQtd() {
		return qtd;
	}
	public void setQtd(int qtd) {
		this.qtd = qtd;
	}
}
