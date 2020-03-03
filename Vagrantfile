# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
 # Workaround to vb-guest-addons error on restarting

  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.

  config.vm.box = "jhipster/jhipster-devbox"
  config.vbguest.iso_path = "https://download.virtualbox.org/virtualbox/%{version}/VBoxGuestAdditions_%{version}.iso"
  config.vbguest.auto_reboot = true

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  config.vm.network "forwarded_port", guest: 8080, host: 8081
  config.vm.network "forwarded_port", guest: 3306, host: 3306

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine and only allow access
  # via 127.0.0.1 to disable public access
  # config.vm.network "forwarded_port", guest: 80, host: 8080, host_ip: "127.0.0.1"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.

  # config.vm.network "private_network", type: "dhcp"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  config.vm.synced_folder "./", "/home/vagrant/jhipster-devbox/project"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:

  config.vm.provider "virtualbox" do |vb|
    # Display the VirtualBox GUI when booting the machine
    vb.gui = true
    vb.cpus = 2
    # Customize the amount of memory on the VM:
    vb.memory = "8096"
    vb.customize ["modifyvm", :id, "--paravirtprovider", "kvm"]
    vb.customize ['modifyvm', :id, '--accelerate3d', 'on']
    vb.customize ['modifyvm', :id, '--graphicscontroller', 'vmsvga']
    vb.customize ['modifyvm', :id, '--hwvirtex', 'on']
    vb.customize ['modifyvm', :id, '--ioapic', 'on']
    vb.customize ['modifyvm', :id, '--vram', '128']
    vb.customize ['modifyvm', :id, '--audio', 'none']
    vb.customize ['modifyvm', :id, '--clipboard', 'bidirectional']
    vb.customize ['modifyvm', :id, '--draganddrop', 'bidirectional']

    vb.customize ['storagectl', :id, "--name", "SATA Controller", "--hostiocache", "on"]
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "Keyboard layout", type: "shell", privileged: false,  run: "once", inline: 'echo "setxkbmap -model abnt2 -layout br" >> /home/vagrant/.profile'
  config.vm.provision "Add keycloak host to DNS", type: "shell", privileged: true,  run: "once", inline: 'echo "127.0.0.1 keycloak" >> /etc/hosts'
  config.vm.provision "Timezone", type: "shell", privileged: true,  run: "once", inline: 'timedatectl set-timezone America/Sao_Paulo'
  config.vm.provision "Update apt-get", type: "shell", privileged: true,  run: "once", inline: 'DEBIAN_FRONTEND=noninteractive apt-get update -y'
  config.vm.provision "Virtualbox-guest-x11", type: "shell", privileged: true,  run: "once", inline: 'yes Y | apt-get install virtualbox-guest-x11'
  config.vm.provision "Snap", type: "shell", privileged: true,  run: "once", inline: 'DEBIAN_FRONTEND=noninteractive apt-get -y -qq install snapd'
  config.vm.provision "Maven", type: "shell", privileged: true,  run: "once", inline: 'DEBIAN_FRONTEND=noninteractive apt-get -y -qq install maven'
  config.vm.provision "Git-cola", type: "shell", privileged: true,  run: "once", inline: 'DEBIAN_FRONTEND=noninteractive apt-get -y -qq install git-cola'
  config.vm.provision "Postman", type: "shell", privileged: true, run: "once", inline: 'snap install postman'
  config.vm.provision "DBeaver", type: "shell", privileged: true, inline: 'snap install dbeaver-ce'
  config.vm.provision "Visual Studio Code", type: "shell", privileged: true, inline: 'snap install code --classic'
end
