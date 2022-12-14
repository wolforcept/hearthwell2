package wolforce.hearthwell.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import wolforce.hearthwell.HearthWell;

public class ParticleEnergyData implements ParticleOptions {

	int color;

	public ParticleEnergyData(int color) {
		this.color = color;
	}

	public ParticleEnergyData() {
		this(HearthWell.getRandomColorOfHearthwell());
	}

	public ParticleEnergyData(int r, int g, int b) {
		this((r << 16) | (g << 8) | b);
	}

	public ParticleEnergyData(int r, int g, int b, int a) {
		this((a << 24) | (r << 16) | (g << 8) | b);
	}

	@Override
	public ParticleType<?> getType() {
		return TYPE;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeInt(color);
	}

	@Override
	public String writeToString() {
		return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()) + " " + color;
	}

	@SuppressWarnings("deprecation")
	public static final Deserializer<ParticleEnergyData> DESERIALIZER = new Deserializer<ParticleEnergyData>() {

		@Override
		public ParticleEnergyData fromCommand(ParticleType<ParticleEnergyData> type, StringReader reader) throws CommandSyntaxException {
//			reader.readString();
//			reader.expect(' ');
			return new ParticleEnergyData(reader.readInt());
		}

		@Override
		public ParticleEnergyData fromNetwork(ParticleType<ParticleEnergyData> type, FriendlyByteBuf buf) {
			return new ParticleEnergyData(buf.readInt());
		}
	};

	public static final ParticleType<ParticleEnergyData> TYPE = new ParticleType<ParticleEnergyData>(false, DESERIALIZER) {
		public Codec<ParticleEnergyData> codec() {
			return CODEC;
		}
	};

	public static final Codec<ParticleEnergyData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			// specify fields
			Codec.INT.fieldOf("color").forGetter(d -> d.color) //
	// specify constructor
	).apply(instance, ParticleEnergyData::new));

//	public static final Codec<ParticleEnergyData> CODEC = RecordCodecBuilder.create(instance -> instance//
//			.group(Codec.INT.fieldOf("color").forGetter(d -> d.color)) //
//			.and(Codec.INT.fieldOf("type").forGetter(d -> d.type))//
//			.apply(instance, ParticleEnergyData::new));
}
