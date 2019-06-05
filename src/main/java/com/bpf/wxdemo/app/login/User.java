package com.bpf.wxdemo.app.login;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户实体类
 * @author baipengfei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
  private String openid;
  private String name;
  private String headImg;
  private Integer sex;
  private String address;
}
