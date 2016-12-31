package net.fortytwo.smsn.brain;

import net.fortytwo.smsn.SemanticSynchrony;
import net.fortytwo.smsn.brain.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventStack {
    private static final SimpleDateFormat EVENT_TIME_FORMAT
            = new SimpleDateFormat("HH:mm:ss' on 'yyyy-MM-dd' 'Z");

    private final int capacity;

    private final LinkedList<Note> stack = new LinkedList<>();

    private final RoutineNamer personNames = new RoutineNamer("person");

    public EventStack(final int capacity) {
        this.capacity = capacity;
    }

    public List<Note> getEvents() {
        return stack;
    }

    public void clear() {
        stack.clear();
        //for (Atom event : stack) {
        //    deleteEvent(event);
        //}
    }

    public Note createGestureEvent(final String expressedBy,
                                   final Date recognizedAt) {
        // TODO: use personal knowledge and Linked Data to find the person's name
        // Use this temporary name only if no actual name is discoverable
        String personName = personNames.getRoutineName(expressedBy);

        //Atom gesture = createAtom();
        Note gesture = new Note();
        gesture.setValue(personName + " did something");

        // note: there will be duplicate people atoms in the in-memory graph
        Note person = new Note();
        person.setValue(personName);
        person.setAlias(expressedBy);

        Note time = new Note();
        time.setValue(EVENT_TIME_FORMAT.format(recognizedAt));

        gesture.addChild(person);
        gesture.addChild(time);

        return gesture;
    }

    public void push(final Note n) {
        setIds(n);

        while (stack.size() >= capacity) {
            stack.removeLast();
            //Atom event = stack.removeLast();
            //deleteEvent(event);
        }

        stack.push(n);
    }

    // make the note look like it came from a graph (so it is compatible with Brain-mode views) by giving it an ID
    private void setIds(final Note n) {
        if (null == n.getId()) {
            n.setId(SemanticSynchrony.createRandomId());
        }

        n.getChildren().forEach(this::setIds);
    }

    // note: instances of this class currently grow without bound
    private class RoutineNamer {
        private final String type;
        private final Map<String, Long> numberByName;
        private long count;

        public RoutineNamer(final String type) {
            this.type = type;
            numberByName = new HashMap<>();
        }

        public String getRoutineName(final String longName) {
            Long number = numberByName.get(longName);
            if (null == number) {
                number = ++count;
                numberByName.put(longName, number);
            }

            return type + " " + number;
        }
    }
}
