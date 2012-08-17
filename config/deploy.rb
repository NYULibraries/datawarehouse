# Call with cap -S branch="<branch-name>" -S user="<user>" [staging|production] deploy
require 'capistrano/ext/multistage'
require 'bundler/capistrano'

set :ssh_options, {:forward_agent => true}
set :application, "datawarehouse"
set :user, "primo"

# Git variables
set :repository,  "git@github.com:NYULibraries/datawarehouse.git"
set :scm, :git
set :scm_username, "Jenkins-NYULib"

# Environments
set :use_sudo, false
set :stages, ["staging", "production"]
set :default_stage, "staging"
set :build_dir, "target"

namespace :deploy do
  desc <<-DESC
    Package the jar with maven.
  DESC
  task :package do
    run_locally "git checkout #{branch} && mvn clean && mvn package"
  end
  
  desc <<-DESC
    Push javadocs to GitHub pages.
  DESC
  task :javadocs do
    run_locally "mvn javadoc:javadoc && git fetch --all && git checkout gh-pages && git pull --rebase origin gh-pages && rm -r apidocs && mv #{build_dir}/site/apidocs apidocs && git add apidocs && git commit -am 'Add javadocs.' && git push origin gh-pages && git checkout #{branch}"
  end
  
  desc <<-DESC
    No restart necessary.
  DESC
  task :restart do
    puts "Skipping restart."
  end

  desc <<-DESC
    No symlink creation necessary.
  DESC
  task :create_symlink do
    puts "Skipping symlink creation."
  end
  
  desc <<-DESC
    No default code updates.
  DESC
  task :update_code do
    puts "Skipping default code update."
  end
  
  desc <<-DESC
    Deploy for enrichment plugins.
  DESC
  task :enrichment do
    set :deploy_to, "/exlibris/primo/p3_1/ng/primo/home/profile/publish/publish/production/conf/plugins/enrichment"
    top.upload File.join(build_dir, "#{application}.jar"), "#{deploy_to}", :via => :scp
  end
  
  before "deploy:enrichment", "deploy:javadocs"
  
  desc <<-DESC
    No finalization steps necessary.
  DESC
  task :finalize_update, :except => { :no_release => true } do
    puts "Skipping finalize update."
  end
  
end