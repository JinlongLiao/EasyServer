// Copyright 2000-2017 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.struct.attr;

import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.modules.decompiler.exps.Exprent;
import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.struct.consts.ConstantPool;
import io.github.jinlongliao.easy.server.mapper.internal.org.jetbrains.java.decompiler.util.DataInputFullStream;

import java.io.IOException;

public class StructAnnDefaultAttribute extends StructGeneralAttribute {

  private Exprent defaultValue;

  @Override
  public void initContent(DataInputFullStream data, ConstantPool pool) throws IOException {
    defaultValue = StructAnnotationAttribute.parseAnnotationElement(data, pool);
  }

  public Exprent getDefaultValue() {
    return defaultValue;
  }
}