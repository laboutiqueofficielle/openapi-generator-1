/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.languages;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.SupportingFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openapitools.codegen.*;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhpClientCodegen extends AbstractPhpCodegen {
    @SuppressWarnings("hiding")
    private static final Logger LOGGER = LoggerFactory.getLogger(PhpClientCodegen.class);

    public static final String COMPOSER_VENDOR_NAME = "composerVendorName";
    public static final String COMPOSER_PROJECT_NAME = "composerProjectName";
    protected String composerVendorName = null;
    protected String composerProjectName = null;

    // Added By LBO from SymfonyServerCodegen.java
    protected HashSet<String> typeHintable;
    protected HashSet<String> defaultIncludesJms;

    public PhpClientCodegen() {
        super();

        // clear import mapping (from default generator) as php does not use it
        // at the moment
        importMapping.clear();

        setInvokerPackage("OpenAPI\\Client");
        setApiPackage(getInvokerPackage() + "\\" + apiDirName);
        setModelPackage(getInvokerPackage() + "\\" + modelDirName);
        setPackageName("OpenAPIClient-php");
        supportsInheritance = true;
        setOutputDir("generated-code" + File.separator + "php");
        modelTestTemplateFiles.put("model_test.mustache", ".php");
        embeddedTemplateDir = templateDir = "php";

        // default HIDE_GENERATION_TIMESTAMP to true
        hideGenerationTimestamp = Boolean.TRUE;

        // provide primitives to mustache template
        List sortedLanguageSpecificPrimitives = new ArrayList(languageSpecificPrimitives);
        Collections.sort(sortedLanguageSpecificPrimitives);
        String primitives = "'" + StringUtils.join(sortedLanguageSpecificPrimitives, "', '") + "'";
        additionalProperties.put("primitives", primitives);

        cliOptions.add(new CliOption(COMPOSER_VENDOR_NAME, "The vendor name used in the composer package name. The template uses {{composerVendorName}}/{{composerProjectName}} for the composer package name. e.g. yaypets. IMPORTANT NOTE (2016/03): composerVendorName will be deprecated and replaced by gitUserId in the next openapi-generator release"));
        cliOptions.add(new CliOption(COMPOSER_PROJECT_NAME, "The project name used in the composer package name. The template uses {{composerVendorName}}/{{composerProjectName}} for the composer package name. e.g. petstore-client. IMPORTANT NOTE (2016/03): composerProjectName will be deprecated and replaced by gitRepoId in the next openapi-generator release"));
        cliOptions.add(new CliOption(CodegenConstants.HIDE_GENERATION_TIMESTAMP, CodegenConstants.ALLOW_UNICODE_IDENTIFIERS_DESC)
                .defaultValue(Boolean.TRUE.toString()));

        // Added by LBO from SymfonyServerCodegen
        defaultIncludes = new HashSet<String>(
            Arrays.asList(
                "\\DateTime",
                "\\SplFileObject"
            )
        );

        // Added by LBO
        defaultIncludesJms = new HashSet<String>(
            Arrays.asList(
                "DateTime"
            )
        );
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    @Override
    public String getName() {
        return "php";
    }

    @Override
    public String getHelp() {
        return "Generates a PHP client library.";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey(COMPOSER_PROJECT_NAME)) {
            this.setComposerProjectName((String) additionalProperties.get(COMPOSER_PROJECT_NAME));
        } else {
            additionalProperties.put(COMPOSER_PROJECT_NAME, composerProjectName);
        }

        if (additionalProperties.containsKey(COMPOSER_VENDOR_NAME)) {
            this.setComposerVendorName((String) additionalProperties.get(COMPOSER_VENDOR_NAME));
        } else {
            additionalProperties.put(COMPOSER_VENDOR_NAME, composerVendorName);
        }

        // Added by LBO : Response.mustache
        supportingFiles.add(new SupportingFile("ApiException.mustache", toSrcPath(invokerPackage, srcBasePath), "ApiException.php"));
        supportingFiles.add(new SupportingFile("Configuration.mustache", toSrcPath(invokerPackage, srcBasePath), "Configuration.php"));
        supportingFiles.add(new SupportingFile("ObjectSerializer.mustache", toSrcPath(invokerPackage, srcBasePath), "ObjectSerializer.php"));
        supportingFiles.add(new SupportingFile("ModelInterface.mustache", toSrcPath(modelPackage, srcBasePath), "ModelInterface.php"));
        supportingFiles.add(new SupportingFile("HeaderSelector.mustache", toSrcPath(invokerPackage, srcBasePath), "HeaderSelector.php"));
        supportingFiles.add(new SupportingFile("Response.mustache", toSrcPath(invokerPackage, srcBasePath), "Response.php"));
        supportingFiles.add(new SupportingFile("composer.mustache", "", "composer.json"));
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
        supportingFiles.add(new SupportingFile("phpunit.xml.mustache", "", "phpunit.xml.dist"));
        supportingFiles.add(new SupportingFile(".travis.yml", "", ".travis.yml"));
        supportingFiles.add(new SupportingFile(".php_cs", "", ".php_cs"));
        supportingFiles.add(new SupportingFile("git_push.sh.mustache", "", "git_push.sh"));

        // Added by LBO from SymfonyServerCodegen.java
        typeHintable = new HashSet<String>(
            Arrays.asList(
                "array",
                "bool",
                "float",
                "int",
                "string"
            )
        );
    }

    public void setComposerVendorName(String composerVendorName) {
        this.composerVendorName = composerVendorName;
    }

    public void setComposerProjectName(String composerProjectName) {
        this.composerProjectName = composerProjectName;
    }

    /**
     * Return the fully-qualified "Model" name for import
     * Added by LBO from SymfonyServerCodegen
     *
     * @param name the name of the "Model"
     * @return the fully-qualified "Model" name for import
     */
    @Override
    public String toModelImport(String name) {
        if ("".equals(modelPackage())) {
            return name;
        } else {
            return modelPackage() + "\\" + name;
        }
    }

    // Added by LBO from SymfonyServerCodegen and modified
    @Override
    public Map<String, Object> postProcessModels(Map<String, Object> objs) {
        objs = super.postProcessModels(objs);

        ArrayList<Object> modelsArray = (ArrayList<Object>) objs.get("models");
        Map<String, Object> models = (Map<String, Object>) modelsArray.get(0);
        CodegenModel model = (CodegenModel) models.get("model");

        // Simplify model var type
        for (CodegenProperty var : model.vars) {
            if (var.dataType != null) {
                // Determine if the parameter type is supported as a type hint and make it available
                // to the templating engine
                String typeHint = getTypeHint(var.dataType);
                String jmsType = getJmsType(var.dataType, var.dataFormat);
                if (!typeHint.isEmpty()) {
                    var.vendorExtensions.put("x-parameterType", typeHint);
                }
                if (!jmsType.isEmpty()) {
                    var.vendorExtensions.put("x-jmsType", jmsType);
                }

                if (var.isContainer) {
                    var.vendorExtensions.put("x-parameterType", getTypeHint(var.dataType + "[]"));
                }

                // Create a variable to display the correct data type in comments for models
                var.vendorExtensions.put("x-commentType", var.dataType);
                if (var.isContainer) {
                    var.vendorExtensions.put("x-commentType", var.dataType + "[]");
                }

                if (var.isBoolean) {
                    var.getter = var.getter.replaceAll("^get", "is");
                }
            }
        }

        return objs;
    }

    // Added by LBO
    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");

        for (CodegenOperation op : operationList) {
            // for API test method name
            // e.g. public function test{{vendorExtensions.x-testOperationId}}()
            op.vendorExtensions.put("x-testOperationId", camelize(op.operationId));

            // Loop through all input parameters to determine, whether we have to import something to
            // make the input type available.
            for (CodegenParameter param : op.allParams) {
                // Determine if the parameter type is supported as a type hint and make it available
                // to the templating engine
                String typeHint = getTypeHint(param.dataType);
                if (!typeHint.isEmpty()) {
                    param.vendorExtensions.put("x-parameterType", typeHint);
                }

                // Create a variable to display the correct data type in comments for interfaces
                param.vendorExtensions.put("x-commentType", param.dataType);
            }

            // Create a variable to display the correct return type in comments for interfaces
            if (op.returnType != null) {
                op.vendorExtensions.put("x-commentType", op.returnType);
            } else {
                op.vendorExtensions.put("x-commentType", "void");
            }
        }

        return objs;
    }

    // Added by LBO from SymfonyServerCodegen
    protected String getTypeHint(String type) {
        // Type hint array types
        if (type.endsWith("[]")) {
            return "array";
        }

        if (type.equals("double")) {
            return "float";
        }

        // Check if the type is a native type that is type hintable in PHP
        if (typeHintable.contains(type)) {
            return type;
        }

        // Default includes are referenced by their fully-qualified class name (including namespace)
        if (defaultIncludes.contains(type)) {
            return type;
        }

        // Model classes are assumed to be imported (Same namespace) and we reference them by their class name
        if (isModelClass(type)) {
            // This parameter is an instance of a model
            return extractSimpleName(type);
        }

        // PHP does not support type hinting for this parameter data type
        return "";
    }

    // Added by LBO
    protected String getJmsType(String type, String format) {
        if (type.startsWith("\\")) {
            type = type.substring(1);
        }

        if (format != null && format.equals("date")) {
            type = "DateTime<'Y-m-d'>";
            return type;
        }

        // Type hint array types
        if (type.endsWith("[]")) {
            type = type.substring(0, type.indexOf("["));

            return "array<"+type+">";
        }

        if (type.equals("double")) {
            return "float";
        }

        // Check if the type is a native type that is type hintable in PHP
        if (typeHintable.contains(type)) {
            if (type.equals("int")) {
                type = "integer";
            } else if (type.equals("bool")) {
                type = "boolean";
            }
            return type;
        }

        // Default includes are referenced by their fully-qualified class name (including namespace)
        if (defaultIncludesJms.contains(type)) {
            return type;
        }

        if (isModelClass(type)) {
            return type;
        }

        // Does not support type hinting for this parameter data type
        return "";
    }

    // Added by LBO from SymfonyServerCodegen
    protected Boolean isModelClass(String type) {
        return Boolean.valueOf(type.contains(modelPackage()));
    }

    // Added by LBO from SymfonyServerCodegen
    protected String extractSimpleName(String phpClassName) {
        if (phpClassName == null) {
            return null;
        }

        final int lastBackslashIndex = phpClassName.lastIndexOf('\\');
        return phpClassName.substring(lastBackslashIndex + 1);
    }
}
