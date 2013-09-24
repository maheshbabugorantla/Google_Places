EPICS APPS Git Branching Model for TADA

There are three types of branched in our project: 
	master: 		
			Description: 		The main branch of the repo, home to "completed" code that has been tested. 
								(Must meet as a team to commit to master)
			Commits allowed: 	No, merging from dev only (merging is done when approved by team)
			Creatable by: 		none (only one)
			Naming Convention:	N/A
			
	development:	
			Description: 		The dev branch of the repo, home to working but untested code. 
								All changed will me put here before merging to master. Commit to this whenever you have working code. 
			Commits allowed: 	Small changes may be committed. Larger additions should use a feature branch then merge into "development"
			Merges to: 			master (when cleared)
			Creatable by: 		none (only one)
			Naming Convention:	N/A
			
	feature: 		
			Description: 		A category of branches. Every large feature (not a simple bugfix) will get a feature branch. 
								These get merged to development. 
			Commits allowed:	yes, any time. (If working on feature branch with team then clarify within team, personal branches are open to preference)
			Creatable by: 		Anyone, anytime. 
			Naming Convention:	Make names clearly represent contents ("barcode" or "scanner" for a barcode scanner, not "my_new_feature")
			Notes:				Feature branches can be deleted for one-off features or kept for longer term features even after the feature is merged into "development"
	

The general workflow will be to take code from "development", propose an improvement (bugfix, new feature, etc) and then create a new branch off of "development" to 
create the feature. Upon completion of the feature, the feature branch is merged into "development". (Note, small single-line fixes may be committed directly to "development")

When development is at a stage that qualifies as a "version" of the app (definition of version is left to the team members and leader) the "development" branch is 
merged into "master" and a tag is added with a version number (v0.1.3, betaV1, v9/18/2013, versioning convention TBD) for easier access down the line. 

More information about the branching model that we have based this on can be found here: http://nvie.com/posts/a-successful-git-branching-model/



				Author: David Tschida, Sophomore CS, 9/18/2013

				
Please note that any changes to the branching model must be discussed with the entire team before being implemented.