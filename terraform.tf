provider "aws" {
  region = "us-east-1"
}

# Security group to allow SSH and HTTP access
resource "aws_security_group" "web_sg" {
  name        = "allow_ssh_http"
  description = "Allow SSH and HTTP traffic"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allow SSH from anywhere (you may restrict in production)
  }

  ingress {
    from_port   = 8080  # Spring Boot default port
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allow HTTP (port 8080) from anywhere
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Launch an EC2 instance with Java 21
resource "aws_instance" "spring_instance" {
  ami           = "ami-0866a3c8686eaeeba"  # AMI with Amazon Linux 2023 (or another AMI supporting Java 21)
  instance_type = "t2.micro"
  key_name      = "SpringStarter-key"  # Use the existing key pair
  security_groups = [aws_security_group.web_sg.name]

  # User data script to install Java 21
  user_data = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo amazon-linux-extras enable corretto21  # Enable Amazon Corretto 21 (Java 21)
              sudo yum install -y java-21-amazon-corretto
              EOF

  tags = {
    Name = "SpringBoot-EC2-Java21"
  }
}

# Output the public IP address of the instance
output "ec2_public_ip" {
  value = aws_instance.spring_instance.public_ip
}
