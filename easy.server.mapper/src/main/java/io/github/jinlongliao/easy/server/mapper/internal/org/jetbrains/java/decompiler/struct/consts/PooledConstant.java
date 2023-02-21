// Copyright 2000-2017 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.struct.consts;

import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.code.CodeConstants;

public class PooledConstant implements CodeConstants {
  public final int type;

  public PooledConstant(int type) {
    this.type = type;
  }

  public void resolveConstant(ConstantPool pool) { }
}
