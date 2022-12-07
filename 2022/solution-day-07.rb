
def run_command(context, cmd)
  target = cmd[4..].strip!
  case target
  when ".."
    context.pop
  when "/"
    context = [""]
  when ""
  else
    context.append(target)
  end
  context
end

def do_me_dumb_style(context, materialized, info)
  maybe_size, maybe_file = info.split(" ")
  unless maybe_size == "dir"
    path = context.dup
    until path.empty?
      path_key = path.join("/")
      unless materialized.has_key?(path_key)
        materialized[path_key] = 0
      end
      materialized[path_key] = materialized[path_key] + maybe_size.to_i
      path.pop
    end
  end
  materialized
end

def ready_to_rock()
  context = [""]
  materialized = {}
  for c in File.readlines("input-day-07.txt")
    if c.start_with?("$")
      context = run_command(context, c)
    else
      materialized = do_me_dumb_style(context, materialized, c)
    end
  end
  materialized
end

# "You need to build a tree, they said!"
def one()
  puts ready_to_rock.values.select{|size| size < 100000}.sum
end

def two()
  dirs = ready_to_rock
  root_size = ready_to_rock.values[0]
  required_space = 70000000 - 30000000 - ready_to_rock.values[0]
  current_space = 70000000
  ready_to_rock.each do |path, size|
    potential_space = required_space + size
    if potential_space > 0 and potential_space < current_space
      current_space = potential_space
    end
  end
  puts current_space - required_space
end

# 1543140
one

# 1117448
two
