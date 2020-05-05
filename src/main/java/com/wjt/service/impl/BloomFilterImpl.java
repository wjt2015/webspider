package com.wjt.service.impl;

import com.wjt.service.BloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BloomFilterImpl implements BloomFilter {
    private final int bits = 100000 << 3;
    private byte[] existTag = new byte[bits >> 3];

    @Override
    public boolean nonExists(String url) {
        int hashCodeA,hashCodeB;
        try {
            hashCodeA = hashCodeA(url);
            hashCodeB = hashCodeB(url);
        } catch (Exception e) {
            log.error("get hashCode error!url={};", url, e);
            return false;
        }
        byte[] tags = getTags(new int[]{hashCodeA, hashCodeB});
        boolean nonExist=false;
        for (byte tag:tags){
            if(tag==0){
                nonExist=true;
            }
        }
        return nonExist;
    }


    private int hashCodeA(final String str) throws Exception {
        if (str == null || str.length() < 1) {
            throw new Exception("hashCode error:" + str);
        }
        int length = str.length();
        String substring = str.substring(0, length >> 1);
        return substring.hashCode() % bits;
    }

    private int hashCodeB(final String str) throws Exception {
        if (str == null || str.length() < 1) {
            throw new Exception("hashCode error:" + str);
        }
        int length = str.length();
        String substring = str.substring(length >> 1, length);
        return substring.hashCode() % bits;
    }

    private void setTag(final int bit) {
        int bits = bit % 8;
        this.existTag[bit >> 3] |= (1 << bits);
    }

    private void setTags(final int[] bits) {
        for (int bit : bits) {
            setTag(bit);
        }
    }

    private byte getTag(final int bit) {
        int bits = bit % 8;
        return (byte) (this.existTag[bit >> 3] >> bits);
    }

    private byte[] getTags(final int[] bits) {
        byte[] tags = new byte[bits.length];
        int k = 0;
        for (int bit : bits) {
            tags[k] = getTag(bit);
            k++;
        }
        return tags;
    }

}
