package com.anread.user.utils;

import java.util.Comparator;

public final class CfiComparators {

    // 公共的、可复用的 CFI 字符串比较器
    public static final Comparator<String> CFI_STRING_COMPARATOR = new CfiStringComparator();

    // 对象比较器：通过提取 CFI 字段进行排序
    public static <T> Comparator<T> byCfi(java.util.function.Function<T, String> cfiExtractor) {
        return Comparator.comparing(cfiExtractor, CFI_STRING_COMPARATOR);
    }

    // 内部实现：比较两个 CFI 字符串
    private static class CfiStringComparator implements Comparator<String> {
        @Override
        public int compare(String cfi1, String cfi2) {
            if (cfi1 == null && cfi2 == null) return 0;
            if (cfi1 == null) return -1;
            if (cfi2 == null) return 1;

            String raw1 = normalize(cfi1);
            String raw2 = normalize(cfi2);

            String main1 = extractMainPath(raw1);
            String main2 = extractMainPath(raw2);

            int cmp = comparePath(main1, main2);
            if (cmp != 0) return cmp;

            String off1 = extractOffset(raw1);
            String off2 = extractOffset(raw2);
            return compareOffset(off1, off2);
        }

        private String normalize(String cfi) {
            if (cfi.startsWith("epubcfi(")) {
                return cfi.substring(8, cfi.length() - 1);
            }
            return cfi;
        }

        private String extractMainPath(String raw) {
            int idx = raw.indexOf(',');
            return idx >= 0 ? raw.substring(0, idx) : raw;
        }

        private String extractOffset(String raw) {
            int idx = raw.indexOf(',');
            return idx >= 0 ? raw.substring(idx) : "";
        }

        // ========== 以下方法与之前相同（略作精简）==========
        private int comparePath(String p1, String p2) {
            String[] a1 = p1.split("/");
            String[] a2 = p2.split("/");
            int len = Math.min(a1.length, a2.length);
            for (int i = 1; i < len; i++) {
                String s1 = a1[i], s2 = a2[i];
                boolean b1 = s1.endsWith("!"), b2 = s2.endsWith("!");
                String n1 = b1 ? s1.substring(0, s1.length() - 1) : s1;
                String n2 = b2 ? s2.substring(0, s2.length() - 1) : s2;
                Integer i1 = tryParse(n1), i2 = tryParse(n2);
                if (i1 != null && i2 != null) {
                    int c = i1.compareTo(i2);
                    if (c != 0) return c;
                    c = Boolean.compare(b1, b2);
                    if (c != 0) return c;
                } else {
                    int c = s1.compareTo(s2);
                    if (c != 0) return c;
                }
            }
            return Integer.compare(a1.length, a2.length);
        }

        private int compareOffset(String o1, String o2) {
            if (o1.isEmpty() && o2.isEmpty()) return 0;
            if (o1.isEmpty()) return -1;
            if (o2.isEmpty()) return 1;
            String[] s1 = o1.substring(1).split(",");
            String[] s2 = o2.substring(1).split(",");
            int n = Math.min(s1.length, s2.length);
            for (int i = 0; i < n; i++) {
                int[] p1 = parseSeg(s1[i]), p2 = parseSeg(s2[i]);
                if (p1 != null && p2 != null) {
                    int c = Integer.compare(p1[0], p2[0]);
                    if (c != 0) return c;
                    c = Integer.compare(p1[1], p2[1]);
                    if (c != 0) return c;
                } else {
                    int c = s1[i].compareTo(s2[i]);
                    if (c != 0) return c;
                }
            }
            return Integer.compare(s1.length, s2.length);
        }

        private Integer tryParse(String s) {
            try { return Integer.parseInt(s); } catch (Exception e) { return null; }
        }

        private int[] parseSeg(String seg) {
            if (seg.startsWith("/") && seg.contains(":")) {
                String[] parts = seg.substring(1).split(":", 2);
                try {
                    return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
                } catch (Exception ignored) {}
            }
            return null;
        }
    }
}