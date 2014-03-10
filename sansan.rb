# coding: utf-8

require "sinatra"
require "mail"
require "cairo"
require "rmagick"
require "json"
require "Base64"

family = "金城"
name = "廣太"
rubi_family = "Kinjo"
rubi_name = "Kodai"
school = "国立沖縄工業高等専門学校"
department = "メディア情報工学科4年"
mail = "info.aokabin@gmail.com"
tel = "090-1943-9827"
pic = Base64.encode64(File.new("qrcode.png").read)
back = Base64.encode64(File.new("back.png").read)

h = {"family" => family, "name" => name, "rubi_family" => rubi_family, "rubi_name" => rubi_name, "school" => school, "department" => department, "mail" => mail, "tel" => tel, "pic" => pic, "back" => back}

configure :production do
end

def make_card (hash)
	w = 637
	h = 385

	surface = Cairo::ImageSurface.new(w, h)
	context = Cairo::Context.new(surface)

	# 白背景
	context.set_source_rgb(1, 1, 1)
	context.rectangle(0, 0, w, h)
	context.fill

	# 背景
	surface2 = Cairo::ImageSurface.from_png('back.png')
	context.set_source(surface2, 0, 0)
	context.paint(0.5)

	# 学校名
	context.set_source_rgb(0, 0, 0)
	context.font_size = 25
	context.move_to(220, 50)
	context.show_text(hash["school"])

	# 所属名
	context.set_source_rgb(0, 0, 0)
	context.font_size = 25
	context.move_to(300, 90)
	context.show_text(hash["department"])

	# 名字
	context.set_source_rgb(0, 0, 0)
	context.font_size = 72
	context.move_to(180, 220)
	context.show_text(hash["family"])

	# 名前
	context.set_source_rgb(0, 0, 0)
	context.font_size = 72
	context.move_to(380, 220)
	context.show_text(hash["name"])

	# 名前のルビ1
	context.set_source_rgb(0, 0, 0)
	context.font_size = 25
	context.move_to(220, 150)
	context.show_text(hash["rubi_family"])

	# 名前のルビ2
	context.set_source_rgb(0, 0, 0)
	context.font_size = 25
	context.move_to(400, 150)
	context.show_text(hash["rubi_name"])

	# 電話番号
	context.set_source_rgb(0, 0, 0)
	context.font_size = 20
	context.move_to(220, 350)
	tel_str = "電話番号：" << hash["tel"]
	context.show_text(tel_str)

	# メールアドレス
	context.set_source_rgb(0, 0, 0)
	context.font_size = 20
	context.move_to(220, 380)
	mail_str = "メールアドレス：" << hash["mail"]
	context.show_text(mail_str)

	# QRコード
	surface2 = Cairo::ImageSurface.from_png('qrcode.png')
	context.set_source(surface2, 15, 240)
	context.paint

	surface.write_to_png('data.png')

	return Base64.encode64(File.new("data.png").read)
end

get "/" do
	erb :make
end

post "/make" do
	data = {"base64" => make_card(h)}
	data.to_json
end