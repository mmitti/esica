$LOAD_PATH.unshift File.join(__dir__, "lib")

require "base64"
require "sinatra"
require "tempfile"
require "business_card"

configure :production do
end

class MyValidator
  def validate(obj)
  end
end

class StringLengthValidator < MyValidator
  def initialize(length)
    @length = length
  end
  def validate(str)
    return str.length <= @length
  end
end

class MailAddressValidator < MyValidator
  def initialize(length)
    @length = length
  end
  def validate(address)
    if address =~ /^[a-zA-Z0-9_¥.¥-]+@[A-Za-z0-9_¥.¥-]+\.[A-Za-z0-9_¥.¥-]+$/
      return address.length <= @length 
    end
    return false
  end
end

class TelephoneValidator < MyValidator
  def initialize(length)
    @length = length
  end
  def validate(telephone)
    if telephone =~ /^((\d+)-?)*\d$/
      return telephone.length <= @length
    end
    return false
  end
end

class FileSizeValidator < MyValidator
  def initialize(limit_str)
    @limit_str = limit_str
  end
  def validate(file)
    return file.length <= @limit_str
  end
end



def validate(request)
  validator = {
    "family" =>      StringLengthValidater.new(10),
    "name" =>        StringLengthValidater.new(10),
    "rubi_family" => StringLengthValidater.new(20),
    "rubi_name" =>   StringLengthValidater.new(20),
    "school" =>      StringLengthValidater.new(20),
    "department" =>  StringLengthValidater.new(20),
    "mail" =>        MailAddressValidater.new(100),
    "tel" =>         TelephoneValidater.new(15),
    "pic" =>         FileSizeValidater.new(100000),     # 1万文字
    "back" =>        FileSizeValidater.new(1000000), # 10万文字
  }
  validator.each {|key, f|
    if !f.validate(request[key])
      return false
    end
  }
  return true
end

helpers do
  def tempfile(name, content)
    if content.empty?
      nil
    else
      tempfile = Tempfile.new(name)
      tempfile.write(Base64.decode(content))
      tempfile
    end
  end
end

get "/business_card.png" do
  BusinessCard.keys.each do |key|
    halt 400 if params[key].nil?
  end
  halt 400 unless validate(params)

  pic = tempfile ["pic", "png"], params["pic"]
  back = tempfile ["back", "png"], params["back"]

  business_card = BusinessCard.new(
    family:      BusinessCard::Family.new(text: params["family"]),
    name:        BusinessCard::Name.new(text: params["name"]),
    rubi_family: BusinessCard::RubiFamily.new(text: params["rubi_family"]),
    rubi_name:   BusinessCard::RubiName.new(text: params["rubi_name"]),
    school:      BusinessCard::School.new(text: params["school"]),
    department:  BusinessCard::Department.new(text: params["department"]),
    tel:         BusinessCard::Tel.new(text: params["tel"]),
    mail:        BusinessCard::Mail.new(text: params["mail"]),
    pic:         BusinessCard::Pic.new(path: pic ? pic.path : nil),
    back:        BusinessCard::Back.new(path: back ? back.path : nil)
  )
  send_file(business_card.make, type: "image/png")
end
