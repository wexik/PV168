package cz.muni.fi.pv168.hotel.gui;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author kurocenko
 */
public abstract class SafeSwingWorker<T,V> extends SwingWorker<T,V> {

    protected T safeGet() {
        try {
            return super.get();
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception thrown in background: " + e.getCause());
        } catch (ExecutionException e) {
            throw new RuntimeException("Operation interrupted");
        }
    }
}
