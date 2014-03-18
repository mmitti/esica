$LOAD_PATH.unshift File.join(__dir__, "lib")

require "base64"
require "sinatra"
require "tempfile"
require "business_card"
require "json"

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
    # if address =~ /^(?:(?:(?:(?:[a-zA-Z0-9_!#\$\%&'*+/=?\^`{}~|\-]+)(?:\.(?:[a-zA-Z0-9_!#\$\%&'*+/=?\^`{}~|\-]+))*)|(?:"(?:\\[^\r\n]|[^\\"])*")))\@(?:(?:(?:(?:[a-zA-Z0-9_!#\$\%&'*+/=?\^`{}~|\-]+)(?:\.(?:[a-zA-Z0-9_!#\$\%&'*+/=?\^`{}~|\-]+))*)|(?:\[(?:\\\S|[\x21-\x5a\x5e-\x7e])*\])))$/ 
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
    "pic" =>         FileSizeValidater.new(100000),  # 10万文字
    "back" =>        FileSizeValidater.new(1000000), # 100万文字
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
  input_data = JSON.parse request.body.read

  BusinessCard.keys.each do |key|
    halt 400 if input_data[key].nil?
  end
  halt 400 unless validate(input_data)

  pic = tempfile ["pic", "png"], input_data["pic"]
  back = tempfile ["back", "png"], input_data["back"]

  if input_data["family"].length + input_data["name"] == 2
    full_name = input_data["family"] + "  " + input_data["name"]    
  else
    full_name = input_data["family"] + " " + input_data["name"]
  end

  business_card = BusinessCard.new(
    name:        BusinessCard::Name.new(text: full_name),
    rubi_family: BusinessCard::RubiFamily.new(text: input_data["rubi_family"]),
    rubi_name:   BusinessCard::RubiName.new(text: input_data["rubi_name"]),
    school:      BusinessCard::School.new(text: input_data["school"]),
    department:  BusinessCard::Department.new(text: input_data["department"]),
    tel:         BusinessCard::Tel.new(text: input_data["tel"]),
    mail:        BusinessCard::Mail.new(text: input_data["mail"]),
    pic:         BusinessCard::Pic.new(path: pic ? pic.path : nil),
    back:        BusinessCard::Back.new(path: back ? back.path : nil)
  )
  send_file(business_card.make, type: "image/png")
end
