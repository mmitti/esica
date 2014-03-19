$LOAD_PATH.unshift File.join(__dir__, "lib")

require "base64"
require "json"
require "tempfile"
require "sinatra/base"
require "sinatra/contrib"
require "business_card"

class Esica < Sinatra::Base
  configure :development do
    register Sinatra::Reloader
  end

  module Validator
    class Base
      def initialize(length)
        @length = length
      end
    end

    class StringLength < Base
      def validate(str)
        str.length <= @length
      end
    end

    class MailAddress < Base
      def validate(address)
        if address =~ /^[a-zA-Z0-9_¥.¥-]+@[A-Za-z0-9_¥.¥-]+\.[A-Za-z0-9_¥.¥-]+$/
          return address.length <= @length 
        end
        false
      end
    end

    class Telephone < Base
      def validate(telephone)
        if telephone =~ /^((\d+)-?)*\d$/
          return telephone.length <= @length
        end
        false
      end
    end

    class FileSize < Base
      def validate(file)
        file.length <= @length
      end
    end
  end

  helpers do
    def tempfile(name, content)
      if content.empty?
        nil
      else
        tempfile = Tempfile.new(name)
        f = open("json.png", "wb")
        f.write(Base64.decode64(content))
        f.close
        tempfile.write(Base64.decode64(content))
        tempfile
      end
    end

    def validate(request)
      validators = {
        "name" =>        Validator::StringLength.new(10),
        "rubi_family" => Validator::StringLength.new(20),
        "rubi_name" =>   Validator::StringLength.new(20),
        "school" =>      Validator::StringLength.new(20),
        "department" =>  Validator::StringLength.new(20),
        "mail" =>        Validator::MailAddress.new(100),
        "tel" =>         Validator::Telephone.new(15),
        "pic" =>         Validator::FileSize.new(100000),  # 10万文字
        "back" =>        Validator::FileSize.new(1000000), # 100万文字
      }
      validators.each do |key, validator|
        return false unless validator.validate(request[key])
      end
      true
    end
  end

  post "/business_card.png" do
    f = open("json.txt", "w")
    f.write("soichiro")
    parameters = JSON.parse(request.body.read)
    p parameters
    f.write(parameters["name"])

    BusinessCard.keys.each do |key|
      halt 400 if parameters[key].nil?
    end
    halt 400 unless validate(parameters)


    f.write(parameters["family"])

    pic  = tempfile ["pic", ".png"], parameters["pic"]
    `open #{pic.path}`
    back = tempfile ["back", ".png"], parameters["back"]
    f.write(parameters["school"])

    business_card = BusinessCard.new(
      name:        BusinessCard::Name.new(text: parameters["name"]),
      rubi_name:   BusinessCard::RubiName.new(text: parameters["rubi_name"]),
      school:      BusinessCard::School.new(text: parameters["school"]),
      department:  BusinessCard::Department.new(text: parameters["department"]),
      tel:         BusinessCard::Tel.new(text: parameters["tel"]),
      mail:        BusinessCard::Mail.new(text: parameters["mail"]),
      pic:         BusinessCard::Pic.new(path: pic ? pic.path : nil),
      back:        BusinessCard::Back.new(path: back ? back.path : nil)
    )
    f.write("business_card init")
    f.close
    # send_file(business_card.make, type: "image/png")
    f = open("data.png", "wb")
    f.write(business_card.make)
    f.close
    str = Base64.encode64(File.new("data.png").read)

    data = {"data" => str}
    data.to_json
  end
end
