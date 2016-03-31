package workhourscontrol.exception;

public class ControleHorasException extends Exception {

	private static final long serialVersionUID = 1L;

	public ControleHorasException() {
		super();
	}

	public ControleHorasException(String message, Throwable e) {
		super(message, e);
	}

	public ControleHorasException(String message) {
		super(message);
	}

	public ControleHorasException(Throwable e) {
		super(e);
	}
}
