package cupid.test.requiresnewdeadlock;

public class TestEvent {

    private Long id;

    public TestEvent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
