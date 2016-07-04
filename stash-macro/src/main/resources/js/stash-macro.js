(function ($) {

    var StashMacro = function () {
    };
    var originalProject;
    var originalRepo;


    StashMacro.prototype.fields = {
        "enum": {
            "project": function (param, options) {
                var paramDiv = AJS.$(Confluence.Templates.MacroBrowser.macroParameterSelect());
                var projectDropDown = AJS.$("select", paramDiv);
                loadProjects(projectDropDown);
                return new AJS.MacroBrowser.Field(paramDiv, projectDropDown, options);
            },
            "repo": function (param, options) {
                var paramDiv = AJS.$(Confluence.Templates.MacroBrowser.macroParameterSelect());
                var repoDropDown = AJS.$("select", paramDiv);
                if (!originalRepo) {
                    repoDropDown.prop('disabled', true);
                } else {
                    repoDropDown.prop('disabled', false);
                    setupRepo(repoDropDown);
                }
                $('select#macro-param-project').change(function () {
                    updateRepos(repoDropDown, this.value);
                });

                return new AJS.MacroBrowser.Field(paramDiv, repoDropDown, options);
            }
        },
        "string": {
            "filter": function (param, options) {
            }
        }
    };

    StashMacro.prototype.beforeParamsSet = function (selectedParams, macroSelected) {
        originalProject = selectedParams.project;
        originalRepo = selectedParams.repo;
        return selectedParams;
    };
    
    StashMacro.prototype.beforeParamsRetrieved = function(params) {
        params.projectName = $('select#macro-param-project option:selected').text();
        params.repoName = $('select#macro-param-repo option:selected').text();
        return params;
    };
    
    function setupProject(projectDropDown) {
        if (originalProject) {
            $('select#macro-param-project').val(originalProject).change();
        }
    }

    function setupRepo(repoDropDown) {
        if (originalRepo) {
            $('select#macro-param-repo').val(originalRepo).change();
        }
    }

    function loadProjects(projectDropDown) {
        AJS.$.ajax({
            async: true,
            url: AJS.contextPath() + "/rest/stash_resource/1.0/stash/projects",
            dataType: 'json',
            timeout: 10000,
            error: function (xhr, textStatus, errorThrown) {
                AJS.logError(errorThrown);
                console.log("THERE WAS AN ERROR: " + errorThrown);
            },
            success: function (response) {
                projectDropDown.empty();
                projectDropDown.append($("<option>None</option>"));
                for (i = 0; i < response.values.length; i++) {
                    projectDropDown.append($("<option></option>").val(response.values[i].key).html(response.values[i].name));
                }
                setupProject(projectDropDown);
            }
        });
    }

    function updateRepos(repoDropDown, projectName) {
        AJS.$.ajax({
            async: true,
            url: AJS.contextPath() + "/rest/stash_resource/1.0/stash/repositories?projectName=" + projectName,
            dataType: 'json',
            timeout: 10000,
            error: function (xhr, textStatus, errorThrown) {
                AJS.logError(errorThrown);
                console.log("THERE WAS AN ERROR:" + errorThrown);
            },
            success: function (response) {
                repoDropDown.empty();
                repoDropDown.append($("<option>None</option>").attr("value", ""));
                repoDropDown.prop('disabled', false);
                for (i = 0; i < response.values.length; i++) {
                    repoDropDown.append($("<option></option>").val(response.values[i].slug).html(response.values[i].name));
                }
                setupRepo(repoDropDown);
            }
        });
    }

    AJS.MacroBrowser.setMacroJsOverride("stash-macro", new StashMacro());

})(AJS.$);