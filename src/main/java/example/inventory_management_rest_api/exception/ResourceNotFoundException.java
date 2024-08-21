package example.inventory_management_rest_api.exception;

/**
 * This class represents custom exception for resource not found in Spring controller.
 */
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
