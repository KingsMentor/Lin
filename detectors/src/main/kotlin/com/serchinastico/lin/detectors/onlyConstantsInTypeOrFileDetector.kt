package com.serchinastico.lin.detectors

import com.android.tools.lint.detector.api.Scope
import com.serchinastico.lin.annotations.Detector
import com.serchinastico.lin.dsl.detector
import com.serchinastico.lin.dsl.issue
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UEnumConstant

@Detector
fun onlyConstantsInTypeOrFile() = detector(
    issue(
        Scope.JAVA_FILE_SCOPE,
        "Using a class/file to store only constants is bad practice",
        """Classes holding only constant values are often a code smell. Constant values should be placed on the class
            | they are being used instead and, if there is more than one place where the constant is used, move them
            | to wherever they make more sense.
        """.trimMargin()
    )
) {
    type { suchThat { it.onlyHasStaticFinalFields } }
}

private inline val UClass.onlyHasStaticFinalFields: Boolean
    get() = methods.all { it.isConstructor } &&
            fields.isNotEmpty() &&
            fields.all { it.isStatic && it.isFinal && it !is UEnumConstant }