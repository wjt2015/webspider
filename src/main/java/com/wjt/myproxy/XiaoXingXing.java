package com.wjt.myproxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XiaoXingXing  implements Person{

    @Override
    public void findLove() {
        log.info("我叫:小星星;性别:女;择偶条件是:");
        log.info("高富帅,有房车,身高180以上,形象气质出众!");
    }
}
