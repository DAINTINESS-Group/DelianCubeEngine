package mainengine.managers;

public interface IBuilder {
	
	ResponseDTO execute(RequestCTO cto) throws Exception;
}
