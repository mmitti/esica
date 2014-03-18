# coding: utf-8

require "sinatra"
require "mail"
require "cairo"
require "rmagick"
require "json"
require "base64"


configure :production do
end

class MyValidater
  def validate(obj)
  end
end

class StringLengthValidater < MyValidater
  def initialize(length)
    @length = length
  end
  def validate(str)
    return str.length <= @length
  end
end

class MailAddressValidater < MyValidater
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

class TelephoneValidater < MyValidater
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

class FileSizeValidater < MyValidater
  def initialize(limit_str)
    @limit_str = limit_str
  end
  def validate(file)
    return file.length <= @limit_str
  end
end

def validate(request)
  validater = {
    "family" => StringLengthValidater.new(10),
    "name" => StringLengthValidater.new(10),
    "rubi_family" => StringLengthValidater.new(20),
    "rubi_name" => StringLengthValidater.new(20),
    "school" => StringLengthValidater.new(20),
    "department" => StringLengthValidater.new(20),
    "mail" => MailAddressValidater.new(100),
    "tel" => TelephoneValidater.new(15),
    "pic" => FileSizeValidater.new(100000),     # 10万文字
    "back" => FileSizeValidater.new(1000000), # 100万文字
  }
  validater.each {|key, f|
    if !f.validate(request[key])
      return false
    end
  }
  return true
end

def make_card (hash)
  width = 637
  height = 385

  back_alpha = 0.5

  font_size_s = 20
  font_size_m = 25
  font_size_l = 72

  school_x = 220
  school_y = 50

  department_x = 300
  department_y = 90

  family_x = 180
  family_y = 220

  name_x = 380
  name_y = 220

  rubi_family_x = 220
  rubi_family_y = 150

  rubi_name_x = 400
  rubi_name_y = 150

  tel_x = 220
  tel_y = 350

  mail_x = 220
  mail_y = 380

  qrcode_x = 15
  qrcode_y = 240


  surface = Cairo::ImageSurface.new(width, height)
  context = Cairo::Context.new(surface)

  # 白背景
  context.set_source_rgb(1, 1, 1)  # 色指定=白色
  context.rectangle(0, 0, width, height)    # 画像サイズ分大きさを確保
  context.fill

  # 背景
  f = open("back.png", "wb")
  f.write(Base64.decode64(hash["back"]))
  f.close

  surface2 = Cairo::ImageSurface.from_png('back.png')
  context.set_source(surface2, 0, 0)   # 原点から画像を描画
  context.paint(back_alpha)

  # 学校名
  context.set_source_rgb(0, 0, 0)   # 色指定 = 黒色
  context.font_size = font_size_m
  context.move_to(school_x, school_y)
  context.show_text(hash["school"])

  # 所属名
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_m
  context.move_to(department_x, department_y)
  context.show_text(hash["department"])

  # 名字
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_l
  context.move_to(family_x, family_y)
  context.show_text(hash["family"])

  # 名前
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_l
  context.move_to(name_x, name_y)
  context.show_text(hash["name"])

  # 名前のルビ1
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_m
  context.move_to(rubi_family_x, rubi_family_y)
  context.show_text(hash["rubi_family"])

  # 名前のルビ2
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_m
  context.move_to(rubi_name_x, rubi_name_y)
  context.show_text(hash["rubi_name"])

  # 電話番号
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_s
  context.move_to(tel_x, tel_y)
  tel_str = "電話番号：" << hash["tel"]
  context.show_text(tel_str)

  # メールアドレス
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_s
  context.move_to(mail_x, mail_y)
  mail_str = "メールアドレス：" << hash["mail"]
  context.show_text(mail_str)

  # QRコード
  f = open("qrcode.png", "wb")
  f.write(Base64.decode64(hash["qrcode"]))
  f.close

  surface2 = Cairo::ImageSurface.from_png('qrcode.png')
  context.set_source(surface2, qrcode_x, qrcode_y)
  context.paint

  surface.write_to_png('data.png')

  return Base64.encode64(File.new("data.png").read)
end

post "/make" do
  data = {"base64" => make_card(h)}
  data.to_json
end

post "/" do
  halt 400 if params["family"].nil?
  halt 400 if params["name"].nil?
  halt 400 if params["rubi_family"].nil?
  halt 400 if params["rubi_name"].nil?
  halt 400 if params["school"].nil?
  halt 400 if params["department"].nil?
  halt 400 if params["mail"].nil?
  halt 400 if params["tel"].nil?
  halt 400 if params["pic"].nil?
  halt 400 if params["back"].nil?
end
