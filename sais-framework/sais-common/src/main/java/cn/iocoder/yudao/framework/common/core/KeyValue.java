package cn.iocoder.yudao.framework.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Key value pair
 *
 * @author Yudao Source Code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K, V> implements Serializable {

 private K key;
 private V value;

}
