ruleset {
    ruleset('rulesets/basic.xml')
    ruleset('rulesets/braces.xml')
    ruleset('rulesets/concurrency.xml')
    ruleset('rulesets/convention.xml')
    ruleset('rulesets/design.xml')
    ruleset('rulesets/dry.xml') {
        DuplicateStringLiteral(enabled: false)
        DuplicateListLiteral(enabled: false)
        DuplicateNumberLiteral(enabled: false)
    }
    ruleset('rulesets/enhanced.xml')
    ruleset('rulesets/formatting.xml') {
        ClassJavadoc(enabled: false)
        SpaceAroundMapEntryColon(enabled: false)
        LineLength(enabled: false)
        Indentation(enabled: false)
    }
    ruleset('rulesets/generic.xml')
    ruleset('rulesets/groovyism.xml')
    ruleset('rulesets/exceptions.xml')
    ruleset('rulesets/imports.xml')
    ruleset('rulesets/size.xml')
    ruleset('rulesets/unnecessary.xml') {
        UnnecessaryBooleanExpression(enabled: false)
        UnnecessaryGetter(enabled: false)
        UnnecessaryPackageReference(enabled: false) // It was changed because of bug in codenarc
    }
    ruleset('rulesets/unused.xml') {
        UnusedObject(enabled: false)
    }
    ruleset('rulesets/security.xml')
}