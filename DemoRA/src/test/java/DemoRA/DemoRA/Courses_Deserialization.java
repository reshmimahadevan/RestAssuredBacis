package DemoRA.DemoRA;

import java.util.List;

public class Courses_Deserialization {

	private List<WebAutomation_Deserialization> webAutomation;
	private List<API_Deserialization> api;
	private List<Mobile_Deserialization> mobile;

	public List<WebAutomation_Deserialization> getWebAutomation() {
		return webAutomation;
	}

	public void setWebAutomation(List<WebAutomation_Deserialization> webAutomation) {
		this.webAutomation = webAutomation;
	}

	public List<API_Deserialization> getApi() {
		return api;
	}

	public void setApi(List<API_Deserialization> api) {
		this.api = api;
	}

	public List<Mobile_Deserialization> getMobile() {
		return mobile;
	}

	public void setMobile(List<Mobile_Deserialization> mobile) {
		this.mobile = mobile;
	}

}