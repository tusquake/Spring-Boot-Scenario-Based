package com.interview.debug.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Set;

/**
 * A safe version of ObjectInputStream that implements Look-ahead Deserialization.
 * It checks the class name before allowing the object to be reconstructed.
 */
public class SafeObjectInputStream extends ObjectInputStream {

    private final Set<String> allowedClasses;

    public SafeObjectInputStream(InputStream in, Set<String> allowedClasses) throws IOException {
        super(in);
        this.allowedClasses = allowedClasses;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        
        // SECURITY CHECK: Only allow specific safe classes
        if (!allowedClasses.contains(className)) {
            throw new SecurityException("Unauthorized deserialization attempt: " + className);
        }
        
        return super.resolveClass(desc);
    }
}
