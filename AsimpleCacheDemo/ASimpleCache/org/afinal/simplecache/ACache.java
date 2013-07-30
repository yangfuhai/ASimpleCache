/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.afinal.simplecache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;

import android.content.Context;

/**
 * @author michael yang（www.yangfuhai.com）
 */
public class ACache {
	private static final int MAX_SIZE = 1000 * 1000 * 30; // 30 mb
	private static final int MAX_COUNT = 1000 * 100; // 10000
	private static Map<String, ACache> instanceMap = new HashMap<String, ACache>();
	private ACacheManager mCache;

	public static ACache get(Context ctx) {
		File f = new File(ctx.getCacheDir(), "ACache");
		return get(f,MAX_SIZE,MAX_COUNT);
	}

	public static ACache get(File cacheDir) {
		return get(cacheDir,MAX_SIZE,MAX_COUNT);
	}

	public static ACache get(Context ctx,int max_zise,int max_count) {
		File f = new File(ctx.getCacheDir(), "ACache");
		return get(f,max_zise,max_count);
	}
	
	public static ACache get(File cacheDir,int max_zise,int max_count) {
		ACache manager = instanceMap.get(cacheDir.getAbsoluteFile() + getPid());
		if (manager == null) {
			manager = new ACache(cacheDir,max_zise,max_count);
			instanceMap.put(cacheDir.getAbsolutePath() + getPid(), manager);
		}
		return manager;
	}

	
	private static String getPid() {
		return "_" + android.os.Process.myPid();
	}

	
	private ACache(File cacheDir,int max_size,int max_count) {
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		mCache = new ACacheManager(cacheDir, max_size, max_count);
	}
	

	// =======================================
	// ========== String类型 读写 ==========
	// =======================================
	public void put(String key, String value) {
		File file = mCache.newFile(key);

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file), 1024);
			out.write(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	
	public void put(String key, String value, int saveTime) {
		put(key, Utils.newStringWithDateInfo(saveTime, value));
	}

	
	
	public String getAsString(String key) {
		File file = mCache.get(key);
		if (!file.exists())
			return null;
		boolean removeFile = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = in.readLine()) != null) {
				readString += currentLine;
			}
			if (!Utils.isDue(readString)) {
				return Utils.clearDateInfo(readString);
			} else {
				removeFile = true;
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}

	// =======================================
	// ========== JSON 读写 ============
	// =======================================

	public void put(String key, JSONObject value) {
		put(key,value.toString());
	}

	public void put(String key, JSONObject value, int saveTime) {
		put(key,value.toString(), saveTime);
	}

	public JSONObject getAsJSONObject(String key) {
		String JSONString = getAsString(key);
		try {
			JSONObject obj = new JSONObject(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// =======================================
	// ========== 二进制 读写 ==========
	// =======================================
	public void put(String key, byte[] value) {
		File file = mCache.newFile(key);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	public void put(byte[] toWrite, String key, int holdTime) {
		put(key, Utils.newByteArrayWithDateInfo(holdTime, toWrite));
	}

	public byte[] getAsBinary(String key) {
		RandomAccessFile RAFile = null;
		boolean removeFile = false;
		try {
			File file = mCache.get(key);
			if (!file.exists())
				return null;
			RAFile = new RandomAccessFile(file, "r");
			byte[] byteArray = new byte[(int) RAFile.length()];
			RAFile.read(byteArray);
			if (!Utils.isDue(byteArray)) {
				return Utils.clearDateInfo(byteArray);
			} else {
				removeFile = true;
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (RAFile != null) {
				try {
					RAFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}

	// =======================================
	// ========== 序列化 读写 ==========
	// =======================================

	public void put(String key, Serializable value) {
		File file = mCache.newFile(key);

		if (file.exists()) {
			file.delete();
		}

		FileOutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new FileOutputStream(file);
			oos = new ObjectOutputStream(os);
			oos.writeObject(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
			}
			try {
				os.close();
			} catch (IOException e) {
			}
		}
	}

	public Object getAsObject(String key) {
		File file = mCache.get(key);
		if (!file.exists())
			return null;

		InputStream is = null;
		ObjectInputStream ois = null;
		try {
			is = new FileInputStream(file);
			ois = new ObjectInputStream(is);

			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public File file(String key) {
		File f = mCache.newFile(key);
		if (f.exists())
			return f;
		return null;
	}

	/**
	 * 移除某个key
	 * 
	 * @param key
	 * @return
	 */
	public boolean remove(String key) {
		return mCache.remove(key);
	}

	/**
	 * 清除所有数据
	 */
	public void clear() {
		mCache.clear();
	}

	
	
	
	
	/**
	 * @title 缓存管理器
	 * @author 杨福海（michael）  www.yangfuhai.com
	 * @version 1.0
	 */
	public class ACacheManager {
		private final AtomicLong cacheSize;
		private final AtomicInteger cacheCount;
		private final int sizeLimit;
		private final int countLimit;
		private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
		protected File cacheDir;

		private ACacheManager(File cacheDir, int sizeLimit, int countLimit) {
			this.cacheDir = cacheDir;
			this.sizeLimit = sizeLimit;
			this.countLimit = countLimit;
			cacheSize = new AtomicLong();
			cacheCount = new AtomicInteger();
			calculateCacheSizeAndCacheCount();
		}

		/**
		 * 计算 cacheSize和cacheCount
		 */
		private void calculateCacheSizeAndCacheCount() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int size = 0;
					int count = 0;
					File[] cachedFiles = cacheDir.listFiles();
					if (cachedFiles != null) {
						for (File cachedFile : cachedFiles) {
							size += calculateSize(cachedFile);
							count += 1;
							lastUsageDates.put(cachedFile,cachedFile.lastModified());
						}
						cacheSize.set(size);
						cacheCount.set(count);
					}
				}
			}).start();
		}

		private void put(File file) {
			int curCacheCount = cacheCount.get();
			while (curCacheCount + 1 > countLimit) {
				long freedSize = removeNext();
				cacheSize.addAndGet(-freedSize);
				
				curCacheCount = cacheCount.addAndGet(-1);
			}
			cacheCount.addAndGet(1);

			long valueSize = calculateSize(file);
			long curCacheSize = cacheSize.get();
			while (curCacheSize + valueSize > sizeLimit) {
				long freedSize = removeNext();
				curCacheSize = cacheSize.addAndGet(-freedSize);
			}
			cacheSize.addAndGet(valueSize);

			Long currentTime = System.currentTimeMillis();
			file.setLastModified(currentTime);
			lastUsageDates.put(file, currentTime);
		}

		private File get(String key) {
			File file = newFile(key);
			Long currentTime = System.currentTimeMillis();
			file.setLastModified(currentTime);
			lastUsageDates.put(file, currentTime);

			return file;
		}

		private File newFile(String key) {
			return new File(cacheDir, key.hashCode() + "");
		}

		private boolean remove(String key) {
			File image = get(key);
			return image.delete();
		}

		private void clear() {
			lastUsageDates.clear();
			cacheSize.set(0);
			File[] files = cacheDir.listFiles();
			if (files != null) {
				for (File f : files) {
					f.delete();
				}
			}
		}

		/**
		 * 存放在最旧的文件
		 * @return
		 */
		private long removeNext() {
			if (lastUsageDates.isEmpty()) {
				return 0;
			}

			Long oldestUsage = null;
			File mostLongUsedFile = null;
			Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
			synchronized (lastUsageDates) {
				for (Entry<File, Long> entry : entries) {
					if (mostLongUsedFile == null) {
						mostLongUsedFile = entry.getKey();
						oldestUsage = entry.getValue();
					} else {
						Long lastValueUsage = entry.getValue();
						if (lastValueUsage < oldestUsage) {
							oldestUsage = lastValueUsage;
							mostLongUsedFile = entry.getKey();
						}
					}
				}
			}

			long fileSize = calculateSize(mostLongUsedFile);
			if (mostLongUsedFile.delete()) {
				lastUsageDates.remove(mostLongUsedFile);
			}
			return fileSize;
		}

		private long calculateSize(File file) {
			return file.length();
		}
	}

	/**
	 * @title 时间计算工具类
	 * @author 杨福海（michael）  www.yangfuhai.com
	 * @version 1.0
	 */
	private static class Utils {

		/**
		 * 是否到期
		 * 
		 * @param str
		 * @return
		 */
		private static boolean isDue(String str) {
			return isDue(str.getBytes());
		}

		/**
		 * 是否到期
		 * 
		 * @param str
		 * @return
		 */
		private static boolean isDue(byte[] data) {
			String[] strs = getDateInfoFromDate(data);
			if (strs != null && strs.length == 2) {
				long saveTime = Long.valueOf(strs[0]);
				long deleteAfter = Long.valueOf(strs[1]);
				if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
					return true;
				}
			}
			return false;
		}

		private static String newStringWithDateInfo(int second, String strInfo) {
			return createDateInfo(second) + strInfo;
		}

		private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
			byte[] data1 = createDateInfo(second).getBytes();
			byte[] retdata = new byte[data1.length + data2.length];
			System.arraycopy(data1, 0, retdata, 0, data1.length);
			System.arraycopy(data2, 0, retdata, data1.length, data2.length);
			return retdata;
		}

		private static String clearDateInfo(String strInfo) {
			if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
				strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,strInfo.length());
			}
			return strInfo;
		}

		private static byte[] clearDateInfo(byte[] data) {
			if (hasDateInfo(data)) {
				return copyOfRange(data, indexOf(data, mSeparator), data.length);
			}
			return data;
		}

		private static boolean hasDateInfo(byte[] data) {
			return data != null && data.length > 15 && data[13] == '-' && indexOf(data, mSeparator) > 14;
		}

		private static String[] getDateInfoFromDate(byte[] data) {
			if (hasDateInfo(data)) {
				String saveDate = new String(copyOfRange(data, 0, 13));
				String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, mSeparator)));
				return new String[] { saveDate, deleteAfter };
			}
			return null;
		}

		private static int indexOf(byte[] data, char c) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] == c) {
					return i;
				}
			}
			return -1;
		}

		private static byte[] copyOfRange(byte[] original, int from, int to) {
			int newLength = to - from;
			if (newLength < 0)
				throw new IllegalArgumentException(from + " > " + to);
			byte[] copy = new byte[newLength];
			System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
			return copy;
		}

		private static final char mSeparator = ' ';
		private static String createDateInfo(int second) {
			return System.currentTimeMillis() + "-" + second + mSeparator;
		}
	}

}
