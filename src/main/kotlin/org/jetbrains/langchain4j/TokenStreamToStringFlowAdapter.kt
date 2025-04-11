package org.jetbrains.langchain4j

import dev.langchain4j.service.TokenStream
import dev.langchain4j.spi.services.TokenStreamAdapter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlinx.coroutines.flow.Flow

class TokenStreamToStringFlowAdapter : TokenStreamAdapter {
    override fun canAdaptTokenStreamTo(type: Type?): Boolean {
        if (type is ParameterizedType) {
            if (type.rawType === Flow::class.java) {
                val typeArguments: Array<Type?> = type.actualTypeArguments
                return typeArguments.size == 1 && typeArguments[0] === String::class.java
            }
        }
        return false
    }

    override fun adapt(tokenStream: TokenStream): Any = tokenStream.asFlow()
}
