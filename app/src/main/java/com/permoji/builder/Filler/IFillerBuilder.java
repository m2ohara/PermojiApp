package com.permoji.builder.Filler;

import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

/**
 * Created by michael on 05/10/18.
 */

public interface IFillerBuilder {

    String addFillerToStatement(TraitResult traitResult, TraitNotifierFillerResult traitNotifierFiller);
}
