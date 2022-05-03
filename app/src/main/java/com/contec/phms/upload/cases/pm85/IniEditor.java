package com.contec.phms.upload.cases.pm85;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.dtools.ini.Commentable;
import u.aly.bs;

public class IniEditor {
    private static final Line BLANK_LINE = new Line() {
        public String toString() {
            return bs.b;
        }
    };
    private char[] commentDelims;
    private String commonName;
    private List sectionOrder;
    private Map sections;

    private interface Line {
        String toString();
    }

    public IniEditor() {
        this((String) null, (char[]) null);
    }

    public IniEditor(String paramString) {
        this(paramString, (char[]) null);
    }

    public IniEditor(char[] paramArrayOfChar) {
        this((String) null, paramArrayOfChar);
    }

    public IniEditor(String paramString, char[] paramArrayOfChar) {
        this.sections = new HashMap();
        this.sectionOrder = new LinkedList();
        if (paramString != null) {
            this.commonName = paramString;
            addSection(this.commonName);
        }
        this.commentDelims = paramArrayOfChar;
    }

    public String get(String paramString1, String paramString2) {
        if (hasSection(paramString1)) {
            Section localSection = getSection(paramString1);
            if (localSection.hasOption(paramString2)) {
                return localSection.get(paramString2);
            }
            if (this.commonName != null) {
                return getSection(this.commonName).get(paramString2);
            }
        }
        return null;
    }

    public void set(String paramString1, String paramString2, String paramString3) {
        if (hasSection(paramString1)) {
            getSection(paramString1).set(paramString2, paramString3);
            return;
        }
        throw new NoSuchSectionException(paramString1);
    }

    public boolean remove(String paramString1, String paramString2) {
        if (hasSection(paramString1)) {
            return getSection(paramString1).remove(paramString2);
        }
        throw new NoSuchSectionException(paramString1);
    }

    public boolean hasOption(String paramString1, String paramString2) {
        return hasSection(paramString1) && getSection(paramString1).hasOption(paramString2);
    }

    public boolean hasSection(String paramString) {
        return this.sections.containsKey(normSection(paramString));
    }

    public boolean addSection(String paramString) {
        String str = normSection(paramString);
        if (hasSection(str)) {
            return false;
        }
        this.sections.put(str, new Section(str, this.commentDelims));
        this.sectionOrder.add(str);
        return true;
    }

    public boolean removeSection(String paramString) {
        String str = normSection(paramString);
        if (this.commonName != null && this.commonName.equals(str)) {
            throw new IllegalArgumentException("Can't remove common section");
        } else if (!hasSection(str)) {
            return false;
        } else {
            this.sections.remove(str);
            this.sectionOrder.remove(str);
            return true;
        }
    }

    public List sectionNames() {
        ArrayList localArrayList = new ArrayList(this.sectionOrder);
        if (this.commonName != null) {
            localArrayList.remove(this.commonName);
        }
        return localArrayList;
    }

    public List optionNames(String paramString) {
        if (hasSection(paramString)) {
            return getSection(paramString).optionNames();
        }
        throw new NoSuchSectionException(paramString);
    }

    public void addComment(String paramString1, String paramString2) {
        if (hasSection(paramString1)) {
            getSection(paramString1).addComment(paramString2);
            return;
        }
        throw new NoSuchSectionException(paramString1);
    }

    public void addBlankLine(String paramString) {
        if (hasSection(paramString)) {
            getSection(paramString).addBlankLine();
            return;
        }
        throw new NoSuchSectionException(paramString);
    }

    public void save(String paramString) throws IOException {
        save(new File(paramString));
    }

    public void save(File paramFile) throws IOException {
        FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
        save((OutputStream) localFileOutputStream);
        localFileOutputStream.close();
    }

    public void save(OutputStream paramOutputStream) throws IOException {
        save(new OutputStreamWriter(paramOutputStream, CPushMessageCodec.GBK));
    }

    public void save(OutputStreamWriter paramOutputStreamWriter) throws IOException {
        PrintWriter localPrintWriter = new PrintWriter(paramOutputStreamWriter, true);
        for (Object section : this.sectionOrder) {
            Section localSection = getSection((String) section);
            localPrintWriter.println(localSection.header());
            localSection.save(localPrintWriter);
        }
    }

    public void load(String paramString) throws IOException {
        load(new File(paramString));
    }

    public void load(File paramFile) throws IOException {
        load((InputStream) new FileInputStream(paramFile));
    }

    public void load(InputStream paramInputStream) throws IOException {
        load(new InputStreamReader(paramInputStream));
    }

    public void load(InputStreamReader paramInputStreamReader) throws IOException {
        int i;
        BufferedReader localBufferedReader = new BufferedReader(paramInputStreamReader);
        String str1 = null;
        while (localBufferedReader.ready()) {
            String str2 = localBufferedReader.readLine().trim();
            if (str2.length() > 0 && str2.charAt(0) == '[' && (i = str2.indexOf(93)) >= 0) {
                str1 = str2.substring(1, i);
                addSection(str1);
            }
            if (str1 != null) {
                getSection(str1).load(localBufferedReader);
            }
        }
    }

    private Section getSection(String paramString) {
        return (Section) this.sections.get(normSection(paramString));
    }

    private static String normSection(String paramString) {
        return paramString.toLowerCase().trim();
    }

    private static String[] toStringArray(Collection paramCollection) {
        Object[] arrayOfObject = paramCollection.toArray();
        String[] arrayOfString = new String[arrayOfObject.length];
        for (int i = 0; i < arrayOfObject.length; i++) {
            arrayOfString[i] = (String) arrayOfObject[i];
        }
        return arrayOfString;
    }

    public static class NoSuchSectionException extends RuntimeException {
        public NoSuchSectionException() {
        }

        public NoSuchSectionException(String paramString) {
        }
    }

    private static class Comment implements Line {
        private static final char DEFAULT_DELIMITER = '#';
        private String comment;
        private char delimiter;

        public Comment(String paramString) {
            this(paramString, DEFAULT_DELIMITER);
        }

        public Comment(String paramString, char paramChar) {
            this.comment = paramString.trim();
            this.delimiter = paramChar;
        }

        public String toString() {
            return String.valueOf(this.delimiter) + " " + this.comment;
        }
    }

    private static class Option implements Line {
        private static final char DEFAULT_SEPARATOR = '=';
        private static final String ILLEGAL_VALUE_CHARS = "\n\r";
        private String name;
        private char separator;
        private String value;

        public Option(String paramString1, String paramString2) {
            this(paramString1, paramString2, DEFAULT_SEPARATOR);
        }

        public Option(String paramString1, String paramString2, char paramChar) {
            if (!validName(paramString1, paramChar)) {
                throw new IllegalArgumentException("Illegal option name:" + paramString1);
            }
            this.name = paramString1;
            this.separator = paramChar;
            set(paramString2);
        }

        public String name() {
            return this.name;
        }

        public String value() {
            return this.value;
        }

        public void set(String paramString) {
            if (paramString == null) {
                this.value = paramString;
                return;
            }
            StringTokenizer localStringTokenizer = new StringTokenizer(paramString.trim(), ILLEGAL_VALUE_CHARS);
            StringBuffer localStringBuffer = new StringBuffer();
            while (localStringTokenizer.hasMoreTokens()) {
                localStringBuffer.append(localStringTokenizer.nextToken());
            }
            this.value = localStringBuffer.toString();
        }

        public String toString() {
            return String.valueOf(this.name) + this.separator + this.value;
        }

        private static boolean validName(String paramString, char paramChar) {
            if (!paramString.trim().equals(bs.b) && paramString.indexOf(paramChar) < 0) {
                return true;
            }
            return false;
        }
    }

    public static class Section {
        private static final char[] DEFAULT_COMMENT_DELIMS = {'#', Commentable.COMMENT_SYMBOL};
        private static final char[] DEFAULT_OPTION_DELIMS = {'=', ':'};
        public static final char HEADER_END = ']';
        public static final char HEADER_START = '[';
        private static final char[] INVALID_NAME_CHARS = {HEADER_START, HEADER_END};
        private static final int NAME_MAXLENGTH = 1024;
        private static final String NEWLINE_CHARS = "\n\r";
        private static final char[] OPTION_DELIMS_WHITESPACE = {' ', 9};
        private char[] commentDelims;
        private char[] commentDelimsSorted;
        private List lines;
        private String name;
        private char[] optionDelims;
        private char[] optionDelimsSorted;
        private Map options;

        public Section(String paramString) {
            this(paramString, (char[]) null);
        }

        public Section(String paramString, char[] paramArrayOfChar) {
            if (!validName(paramString)) {
                throw new IllegalArgumentException("Illegal section name:" + paramString);
            }
            this.name = paramString;
            this.options = new HashMap();
            this.lines = new LinkedList();
            this.optionDelims = DEFAULT_OPTION_DELIMS;
            this.commentDelims = paramArrayOfChar == null ? DEFAULT_COMMENT_DELIMS : paramArrayOfChar;
            this.optionDelimsSorted = new char[this.optionDelims.length];
            System.arraycopy(this.optionDelims, 0, this.optionDelimsSorted, 0, this.optionDelims.length);
            this.commentDelimsSorted = new char[this.commentDelims.length];
            System.arraycopy(this.commentDelims, 0, this.commentDelimsSorted, 0, this.commentDelims.length);
            Arrays.sort(this.optionDelimsSorted);
            Arrays.sort(this.commentDelimsSorted);
        }

        public List optionNames() {
            LinkedList localLinkedList = new LinkedList();
            for (Object localObject : this.lines) {
                if (localObject instanceof Option) {
                    localLinkedList.add(((Option) localObject).name());
                }
            }
            return localLinkedList;
        }

        public boolean hasOption(String paramString) {
            return this.options.containsKey(normOption(paramString));
        }

        public String get(String paramString) {
            String str = normOption(paramString);
            if (hasOption(str)) {
                return getOption(str).value();
            }
            return null;
        }

        public void set(String paramString1, String paramString2) {
            set(paramString1, paramString2, this.optionDelims[0]);
        }

        public void set(String paramString1, String paramString2, char paramChar) {
            String str = normOption(paramString1);
            if (hasOption(str)) {
                getOption(str).set(paramString2);
                return;
            }
            Option localOption = new Option(str, paramString2, paramChar);
            this.options.put(str, localOption);
            this.lines.add(localOption);
        }

        public boolean remove(String paramString) {
            String str = normOption(paramString);
            if (!hasOption(str)) {
                return false;
            }
            this.lines.remove(getOption(str));
            this.options.remove(str);
            return true;
        }

        public void addComment(String paramString) {
            addComment(paramString, this.commentDelims[0]);
        }

        public void addComment(String paramString, char paramChar) {
            StringTokenizer localStringTokenizer = new StringTokenizer(paramString.trim(), NEWLINE_CHARS);
            while (localStringTokenizer.hasMoreTokens()) {
                this.lines.add(new Comment(localStringTokenizer.nextToken(), paramChar));
            }
        }

        public void addBlankLine() {
            this.lines.add(IniEditor.BLANK_LINE);
        }

        public void load(BufferedReader paramBufferedReader) throws IOException {
            int i1;
            while (paramBufferedReader.ready()) {
                paramBufferedReader.mark(1024);
                String str = paramBufferedReader.readLine().trim();
                if (str.length() > 0 && str.charAt(0) == '[') {
                    paramBufferedReader.reset();
                    return;
                } else if (str.equals(bs.b)) {
                    addBlankLine();
                } else {
                    int i = Arrays.binarySearch(this.commentDelimsSorted, str.charAt(0));
                    if (i >= 0) {
                        addComment(str.substring(1), this.commentDelimsSorted[i]);
                    } else {
                        int i2 = -1;
                        int k = -1;
                        int n = str.length();
                        for (int m = 0; m < n && i2 < 0; m++) {
                            if (Arrays.binarySearch(this.optionDelimsSorted, str.charAt(m)) < 0) {
                                if (Arrays.binarySearch(OPTION_DELIMS_WHITESPACE, str.charAt(m)) >= 0) {
                                    i1 = 1;
                                } else {
                                    i1 = 0;
                                }
                                if (i1 == 0 && k >= 0) {
                                    break;
                                } else if (i1 != 0) {
                                    k = m;
                                }
                            } else {
                                i2 = m;
                            }
                        }
                        if (i2 != 0) {
                            if (i2 >= 0) {
                                set(str.substring(0, i2), str.substring(i2 + 1), str.charAt(i2));
                            } else if (k < 0) {
                                set(str, bs.b);
                            } else {
                                set(str.substring(0, k), str.substring(k + 1));
                            }
                        }
                    }
                }
            }
        }

        public void save(PrintWriter paramPrintWriter) throws IOException {
            for (Object println : this.lines) {
                paramPrintWriter.println(println);
            }
            if (paramPrintWriter.checkError()) {
                throw new IOException();
            }
        }

        private Option getOption(String paramString) {
            return (Option) this.options.get(paramString);
        }

        private String header() {
            return String.valueOf(HEADER_START) + this.name + HEADER_END;
        }

        private static boolean validName(String paramString) {
            if (paramString.trim().equals(bs.b)) {
                return false;
            }
            for (char indexOf : INVALID_NAME_CHARS) {
                if (paramString.indexOf(indexOf) >= 0) {
                    return false;
                }
            }
            return true;
        }

        private static String normOption(String paramString) {
            return paramString.toLowerCase().trim();
        }
    }
}
