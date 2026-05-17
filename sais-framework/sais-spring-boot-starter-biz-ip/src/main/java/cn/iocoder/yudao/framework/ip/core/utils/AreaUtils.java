package cn.iocoder.yudao.framework.ip.core.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.enums.AreaTypeEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.findFirst;

/**
 * Regional tools
 *
 * @author Yudao Source Code
 */
@Slf4j
public class AreaUtils {

 /**
     * Initialize SEARCHER
 */
 @SuppressWarnings("InstantiationOfUtilityClass")
 private final static AreaUtils INSTANCE = new AreaUtils();

 /**
     * Area memory cache to improve access speed
 */
 private static Map<Integer, Area> areas;

 private AreaUtils() {
 long now = System.currentTimeMillis();
 areas = new HashMap<>();
        areas.put(Area.ID_GLOBAL, new Area(Area.ID_GLOBAL, "worldwide", 0,
 null, new ArrayList<>()));
        // Load data from csv
 List<CsvRow> rows = CsvUtil.getReader().read(ResourceUtil.getUtf8Reader("area.csv")).getRows();
        rows.remove(0); // delete header
 for (CsvRow row: rows) {
            // Create an Area object
 Area area = new Area(Integer.valueOf(row.get(0)), row.get(1), Integer.valueOf(row.get(2)),
 null, new ArrayList<>());
            // Add to areas
 areas.put(area.getId(), area);
 }

        // Build a parent-child relationship: Because there is no parentID field in Area, it needs to be read repeatedly
 for (CsvRow row: rows) {
            Area area = areas.get(Integer.valueOf(row.get(0))); // Own
            Area parent = areas.get(Integer.valueOf(row.get(3))); // father
            Assert.isTrue(area != parent, "{}: The parent and child nodes are the same", area.getName());
 area.setParent(parent);
 parent.getChildren().add(area);
 }
        log.info("Started loading AreaUtils successfully, taking ({}) milliseconds", System.currentTimeMillis() - now);
 }

 /**
     * Get the area corresponding to the specified number
 *
     * @param id area number
     * @return area
 */
 public static Area getArea(Integer id) {
 return areas.get(id);
 }

 /**
     * Get the number corresponding to the specified area
 *
     * @param pathStr Regional path, for example: Henan Province/Shijiazhuang City/Xinhua District
     * @return area
 */
 public static Area parseArea(String pathStr) {
 String[] paths = pathStr.split("/");
 Area area = null;
 for (String path: paths) {
 if (area == null) {
 area = findFirst(areas.values(), item -> item.getName().equals(path));
 } else {
 area = findFirst(area.getChildren(), item -> item.getName().equals(path));
 }
 }
 return area;
 }

 /**
     * Get the full path names of all nodes, such as: Henan Province/Shijiazhuang City/Xinhua District
 *
     * @param areas area tree
     * @return Full path names of all nodes
 */
 public static List<String> getAreaNodePathList(List<Area> areas) {
 List<String> paths = new ArrayList<>();
 areas.forEach(area -> getAreaNodePathList(area, "", paths));
 return paths;
 }

 /**
     * Constructs the full path names of all nodes of a tree and stores them in the form "ancestor/parent/child"
 *
     * @param node parent node
     * @param path full path name
     * @param paths Full path name list, province/city/region
 */
 private static void getAreaNodePathList(Area node, String path, List<String> paths) {
 if (node == null) {
 return;
 }
        // Build the path of the current node
 String currentPath = path.isEmpty() ? node.getName(): path + "/" + node.getName();
 paths.add(currentPath);
        // Recursively traverse child nodes
 for (Area child: node.getChildren()) {
 getAreaNodePathList(child, currentPath, paths);
 }
 }

 /**
     * Format area
 *
     * @param id area number
     * @return Formatted area
 */
 public static String format(Integer id) {
 return format(id, " ");
 }

 /**
     * Format area
 *
     * For example:
     * 1. When ID = "Jing'an District": Shanghai Shanghai Jing'an District
     * 2. When ID = "Shanghai City": Shanghai Shanghai City
     * 3. When ID = "Shanghai": Shanghai
     * 4. When ID = "United States": United States
     * When the region is in China, China is not displayed by default.
 *
     * @param id area number
     * @param separator delimiter
     * @return Formatted area
 */
 public static String format(Integer id, String separator) {
        // get area
 Area area = areas.get(id);
 if (area == null) {
 return null;
 }

        // format
 StringBuilder sb = new StringBuilder();
        for (int i = 0; i < AreaTypeEnum.values().length; i++) { // AvoID endless loops
 sb.insert(0, area.getName());
            // "Recursive" parent node
 area = area.getParent();
 if (area == null
                    || ObjectUtils.equalsAny(area.getId(), Area.ID_GLOBAL, Area.ID_CHINA)) { // Skip the situation where the parent node is China
 break;
 }
 sb.insert(0, separator);
 }
 return sb.toString();
 }

 /**
     * Get a list of areas of a specified type
 *
     * @param type Area type
     * @param func conversion function
     * @param <T> result type
     * @return Area list
 */
 public static <T> List<T> getByType(AreaTypeEnum type, Function<Area, T> func) {
 return convertList(areas.values(), func, area -> type.getType().equals(area.getType()));
 }

 /**
     * Obtain the upper-level area number based on the area number and upper-level area type
 *
     * @param id area number
     * @param type Area type
     * @return Superior area number
 */
 public static Integer getParentIdByType(Integer id, @NonNull AreaTypeEnum type) {
 for (int i = 0; i < Byte.MAX_VALUE; i++) {
 Area area = AreaUtils.getArea(id);
 if (area == null) {
 return null;
 }
            // Case 1: Match, return it
 if (type.getType().equals(area.getType())) {
 return area.getId();
 }
            // Case 2: Find the root node and return empty
 if (area.getParent() == null || area.getParent().getId() == null) {
 return null;
 }
            // Others: continue to search upwards
 id = area.getParent().getId();
 }
 return null;
 }

}
