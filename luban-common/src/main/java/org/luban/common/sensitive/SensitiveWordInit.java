package org.luban.common.sensitive;

import org.luban.common.encrypt.Base64Util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @version 1.0
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
public class SensitiveWordInit {

    public HashMap sensitiveWordMap;

    public SensitiveWordInit() {
        super();
    }

    public Map initKeyWord() {
        try {
            Set<String> keyWordSet = readSensitiveWordFile();
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }


    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        InputStreamReader read = new InputStreamReader(SensitiveWordInit.class.getResourceAsStream("pub_banned_words.txt"));
        try {
            set = new HashSet<String>();
            BufferedReader bufferedReader = new BufferedReader(read);
            FileWriter fileWriter = new FileWriter("d:/b.txt");
            String txt = null;
            while ((txt = bufferedReader.readLine()) != null) {
                set.add(Base64Util.decode(txt));
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();
        }
        return set;
    }
}
