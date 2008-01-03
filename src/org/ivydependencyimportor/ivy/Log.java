package org.ivydependencyimportor.ivy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class Log {
    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
    public static final boolean IS_DEBUG = true;

    static {
//        IS_DEBUG = Boolean.getBoolean(System.getProperty("debug"));
    }

    public static void log(Object o) {
        if (IS_DEBUG) {
            System.out.println(df.format(new Date()) + o);
        }
    }

    public static void log(long o) {
        if (IS_DEBUG) {
            System.out.println(df.format(new Date()) + o);
        }
    }
}