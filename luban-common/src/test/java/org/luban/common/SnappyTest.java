package org.luban.common;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * User:shijingui
 * Date:2019/2/20
 *  
 */
public class SnappyTest {

    public static void main(String[] args) {
        String aa = "SELECT item_first_cate_cd AS itemFirstCateCd,item_first_cate_name AS itemFirstCateName,item_second_cate_cd AS itemSecondCateCd,item_second_cate_name AS itemSecondCateName,item_third_cate_cd AS itemThirdCateCd,item_third_cate_name AS itemThirdCateName,brand_code AS brandCode,brand_name AS brandName FROM app_vdp_flmc_brand_business_authority_day WHERE dt = #{dt} AND brand_business_code =";
        System.out.println(aa.getBytes().length);
        byte[] efa =compressHtml(aa);

        System.err.println(efa.length);
    }


    public static byte[] compressHtml(String html) {
        try {
            return Snappy.compress(html.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompressHtml(byte[] bytes) {
        try {
            return new String(Snappy.uncompress(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
